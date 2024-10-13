package com.PSM.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/home")
    public String mainPage() {
        return "main"; // Здесь предполагается, что есть файл main.html в папке templates
    }
}
