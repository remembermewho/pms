package com.PSM.demo.controller;

import com.PSM.demo.model.Project;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.service.ProjectService;
import com.PSM.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;
    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public String viewProjects(Model model, Authentication authentication) {
        // Получаем текущего пользователя по имени
        UserEntity currentUser = userService.findByUsername(authentication.getName());

        // Получаем проекты, которыми управляет пользователь, и проекты, в которых он является исполнителем
        List<Project> managedProjects = projectService.findByManager(currentUser);
        List<Project> assignedProjects = projectService.findByAssignedUser(currentUser);

        // Добавляем списки проектов в модель
        model.addAttribute("managedProjects", managedProjects);
        model.addAttribute("assignedProjects", assignedProjects);

        return "projects"; // Возвращаем имя шаблона для отображения
    }

    @GetMapping("/create")
    public String createProjectPage(Model model) {
        model.addAttribute("project", new Project());
        List<UserEntity> users = userService.getAllUsers(); // Получение всех пользователей для выбора исполнителей
        model.addAttribute("users", users);
        return "create_project"; // Шаблон для создания проекта
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project, @RequestParam Long managerId) {
        // Получите менеджера по ID
        UserEntity manager = userService.findById(managerId);
        if (manager != null) {
            project.setManager(manager); // Установите менеджера
        } else {
            // Обработка случая, если менеджер не найден (например, выбросить исключение или вернуть ошибку)
        }

        projectService.saveProject(project); // Сохранение проекта
        return "redirect:/projects"; // Перенаправление на страницу проектов после успешного создания
    }
// ===============================================================================

    @GetMapping("/{id}/editAssignees")
    public String editAssignees(@PathVariable Long id, Model model) {
        Project project = projectService.findById(id);
        System.out.println("==============================================");
        System.out.println(project.toString());
        System.out.println("==============================================");
        List<UserEntity> allUsers = userService.getAllUsers();  // Например, получение всех пользователей
        model.addAttribute("project", project);
        model.addAttribute("allUsers", allUsers);  // Передаем всех пользователей в шаблон
        return "editAssignees";  // Шаблон для изменения исполнителей
    }

    @PostMapping("/{projectId}/addAssignees")
    public String addAssignees(@PathVariable Long projectId, @RequestParam List<Long> assigneeIds) {
        Project project = projectService.findById(projectId);
        List<UserEntity> assignees = userService.findUsersByIds(assigneeIds);  // Предположим, что есть такой метод
        projectService.addAssigneesToProject(project, assignees);  // Добавляем исполнителей к проекту
        return "redirect:/projects/" + projectId + "/editAssignees";
    }

    @GetMapping("/{projectId}/removeAssignee/{userId}")
    public String removeAssignee(@PathVariable Long projectId, @PathVariable Long userId) {
        // Найти проект и пользователя
        Project project = projectService.findById(projectId);
        UserEntity user = userService.findById(userId);

        // Удалить пользователя из исполнителей проекта
        projectService.removeAssigneeFromProject(project, user);

        // Перенаправление на страницу редактирования исполнителей
        return "redirect:/projects/" + projectId + "/editAssignees";
    }
//=================================================================================
//    @GetMapping("/{projectId}/edit")
//    public String editProjectPage(@PathVariable Long projectId, Model model) {
//        Project project = projectService.findProjectById(projectId);
//        List<UserEntity> users = userService.getAllUsers();
//        model.addAttribute("project", project);
//        model.addAttribute("users", users);
//        return "edit_project"; // Шаблон для редактирования проекта
//    }
//
//    @PostMapping("/{projectId}/edit")ss
//    public String editProject(@PathVariable Long projectId, @ModelAttribute Project project) {
//        projectService.updateProject(projectId, project); // Логика обновления проекта
//        return "redirect:/projects"; // Перенаправление на страницу проектов
//    }
//
//    @PostMapping("/{projectId}/add-assignee")
//    public String addAssignee(@PathVariable Long projectId, @RequestParam Long userId) {
//        projectService.addAssigneeToProject(projectId, userId); // Логика добавления исполнителя
//        return "redirect:/projects/" + projectId + "/edit"; // Перенаправление на страницу редактирования проекта
//    }
//
//    @PostMapping("/{projectId}/remove-assignee")
//    public String removeAssignee(@PathVariable Long projectId, @RequestParam Long userId) {
//        projectService.removeAssigneeFromProject(projectId, userId); // Логика удаления исполнителя
//        return "redirect:/projects/" + projectId + "/edit"; // Перенаправление на страницу редактирования проекта
//    }

}
