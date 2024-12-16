package com.PSM.demo.controller;

import com.PSM.demo.model.Project;
import com.PSM.demo.model.Task;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.service.ProjectService;
import com.PSM.demo.service.TaskService;
import com.PSM.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService, TaskService taskService) {
        this.projectService = projectService;
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping
    public String viewProjects(@RequestParam(value = "search", required = false) String searchTerm,
                               Model model, Authentication authentication) {
        UserEntity currentUser = userService.findByUsername(authentication.getName());

        List<Project> managedProjects = projectService.findManagedProjects(currentUser, searchTerm);
        List<Project> assignedProjects = projectService.findAssignedProjects(currentUser, searchTerm);

        model.addAttribute("managedProjects", managedProjects);
        model.addAttribute("assignedProjects", assignedProjects);
        model.addAttribute("searchTerm", searchTerm); // Передаём значение поиска в шаблон

        return "projects";
    }


    @GetMapping("/create")
    public String createProjectPage(Model model) {
        model.addAttribute("project", new Project());
        List<UserEntity> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "create_project";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project, @RequestParam Long managerId) {
        UserEntity manager = userService.findById(managerId);
        if (manager != null) {
            project.setManager(manager);
            projectService.saveProject(project);
            return "redirect:/projects";
        } else {
            return "redirect:/projects/create?error=managerNotFound";
        }
    }

    @GetMapping("/{id}/editAssignees")
    public String editAssignees(@PathVariable Long id, Model model) {
        Project project = projectService.findById(id);
        List<UserEntity> allUsers = userService.getAllUsers();
        model.addAttribute("project", project);
        model.addAttribute("allUsers", allUsers);
        return "editAssignees";
    }

    @PostMapping("/{projectId}/addAssignees")
    public String addAssignees(@PathVariable Long projectId, @RequestParam List<Long> assigneeIds) {
        Project project = projectService.findById(projectId);
        List<UserEntity> assignees = userService.findUsersByIds(assigneeIds);
        projectService.addAssigneesToProject(project, assignees);
        return "redirect:/projects/" + projectId + "/editAssignees";
    }

    @GetMapping("/{projectId}/removeAssignee/{userId}")
    public String removeAssignee(@PathVariable Long projectId, @PathVariable Long userId) {
        Project project = projectService.findById(projectId);
        UserEntity user = userService.findById(userId);
        projectService.removeAssigneeFromProject(project, user);
        return "redirect:/projects/" + projectId + "/editAssignees";
    }

    @GetMapping("/{projectId}/viewTasks")
    public String viewTasksForProject(@PathVariable Long projectId, Model model, Authentication authentication) {
        UserEntity currentUser = userService.findByUsername(authentication.getName());

        Project project = projectService.findById(projectId);
        List<Task> tasks = taskService.getTasksByAssigneeAndProject(currentUser.getId(), projectId);

        model.addAttribute("project", project);
        model.addAttribute("tasks", tasks);

        return "view_tasks_for_project";
    }
}
