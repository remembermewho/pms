package com.PSM.demo.controller;

import com.PSM.demo.dto.UserAuthDTO;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class AuthController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @PostMapping("/auth/login")
    public String loginUser(@ModelAttribute UserAuthDTO loginDTO, RedirectAttributes redirectAttributes) {
        try {
            if (userService.authenticateUser(loginDTO)) {
                return "successAuth"; // Переход на страницу успешной авторизации
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage()); // Добавляем сообщение об ошибке
        }
        return "redirect:/auth/login"; // Возврат на страницу входа
    }


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
    public String registerUser(@ModelAttribute UserRegistrationDTO registrationDTO, RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(registrationDTO);
            redirectAttributes.addFlashAttribute("message", "Пользователь успешно зарегистрирован");
            return "successReg"; // Перенаправление на страницу успеха
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/auth/register"; // Перенаправление обратно на страницу регистрации с сообщением об ошибке
        }
    }

    @GetMapping("/auth/success")
    public String successPage() {
        return "successReg"; // Имя файла success.html
    }


}
