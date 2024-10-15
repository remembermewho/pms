package com.PSM.demo.controller;

import com.PSM.demo.model.Project;
import com.PSM.demo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public String projectsPage(Model model) {
        // Получаем информацию о текущем пользователе
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Получаем все проекты и добавляем их в модель
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        model.addAttribute("username", username);

        return "projects"; // Возвращает страницу с проектами
    }

    @GetMapping("/projects/create")
    public String createProjectPage(Model model) {
        model.addAttribute("project", new Project());
        return "create_project"; // Возвращает страницу для создания нового проекта
    }

    @PostMapping("/projects/create")
    public String createProject(Project project) {
        projectService.createProject(project);
        return "redirect:/projects"; // Перенаправляет на страницу проектов после создания
    }
}
