package com.PSM.demo.controller;


import com.PSM.demo.dto.UserRegistrationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String mainPage() {
        return "main"; // Здесь предполагается, что есть файл main.html в папке templates
    }

    @GetMapping("/auth/register")
    public String registrationPage(Model model) {
        model.addAttribute("users", new UserRegistrationDTO());
        return "register"; // Страница register.html
    }

    @GetMapping("/auth/login")
    public String loginPage() {
        return "login"; // Страница login.html
    }

    @GetMapping("/personal-cabinet")
    public String personalCabinet() {
        return "personal_cabinet"; // Страница personal_cabinet.html
    }

    @PostMapping("/auth/logout")
    public String logout() {
        return "redirect:/"; // Перенаправление на главную страницу после выхода
    }

    @GetMapping("/auth/logout-success")
    public String logoutPage() {
        return "logout-success"; // Отображает страницу с информацией о выходе
    }
}
