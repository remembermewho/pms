package com.PSM.demo.controller;

import com.PSM.demo.dto.UserAuthDTO;
import com.PSM.demo.dto.UserLoginDTO;
import com.PSM.demo.dto.UserRegistrationDTO;
import com.PSM.demo.model.Role;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.repository.RoleRepository;
import com.PSM.demo.repository.UserRepository;
import com.PSM.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;


    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute UserLoginDTO userLoginDTO, RedirectAttributes redirectAttributes) {
        try {
            // Аутентификация пользователя
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/personal-cabinet";  // Переход в личный кабинет после успешной авторизации
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Неправильное имя пользователя или пароль.");
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserRegistrationDTO registrationDTO, RedirectAttributes redirectAttributes) {
        try {
            // Проверка, существует ли уже пользователь с таким именем
            if (userRepository.existsByUsername(registrationDTO.getUsername())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Имя пользователя занято.");
                return "redirect:/auth/register"; // Возврат на страницу регистрации с сообщением об ошибке
            }

            // Создание нового пользователя
            UserEntity user = new UserEntity();
            user.setUsername(registrationDTO.getUsername());
            user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            user.setEmail(registrationDTO.getEmail());

            // Поиск роли
            Optional<Role> roleOpt = roleRepository.findByRoleName(registrationDTO.getRoleName());
            if (roleOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Роль не найдена.");
                return "redirect:/auth/register"; // Возврат на страницу регистрации с сообщением об ошибке
            }

            Role role = roleOpt.get();
            user.setRole(role);

            // Сохранение пользователя в базе данных
            userRepository.save(user);

            // Добавление сообщения об успешной регистрации
            redirectAttributes.addFlashAttribute("message", "Пользователь успешно зарегистрирован. Пожалуйста, войдите в свой аккаунт.");

            // Перенаправление на страницу входа
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            // Обработка ошибок
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/auth/register"; // Возврат на страницу регистрации с сообщением об ошибке
        }
    }

}
