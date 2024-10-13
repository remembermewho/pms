package com.PSM.demo.controller;

import com.PSM.demo.dto.UserRegistrationDTO;
import com.PSM.demo.model.Role;
import com.PSM.demo.model.User;
import com.PSM.demo.repository.RoleRepository;
import com.PSM.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class AuthController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping("/auth/login")
    public String loginPage() {
        return "login"; // Имя файла login.html
    }

    // Получение страницы регистрации
    @GetMapping("/auth/register")
    public String registrationPage(Model model) {

        List<Role> roles = roleRepository.findAll(); // Получаем все роли из базы
        model.addAttribute("roles", roles); // Добавляем роли в модель
        model.addAttribute("users", new UserRegistrationDTO());
        return "register"; // Имя файла register.html
    }

    // Регистрация пользователя
    @PostMapping("/auth/register")
    public String registerUser(@ModelAttribute UserRegistrationDTO registrationDTO) {
        userService.registerUser(registrationDTO);
        return "redirect:/auth/login"; // Перенаправление на страницу входа после успешной регистрации
    }

    // Авторизация пользователя
    @PostMapping("/auth/login")
    public ResponseEntity<String> loginUser(@RequestBody UserRegistrationDTO loginDTO) {
        // Здесь может быть логика авторизации
        return ResponseEntity.ok("Пользователь успешно авторизован");
    }


}
