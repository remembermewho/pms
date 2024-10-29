package com.PSM.demo.controller;

import com.PSM.demo.enums.TaskStatus;
import com.PSM.demo.model.Project;
import com.PSM.demo.model.Task;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.service.ProjectService;
import com.PSM.demo.service.TaskService;
import com.PSM.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final ProjectService projectService;

    public TaskController(TaskService taskService, UserService userService, ProjectService projectService) {
        this.taskService = taskService;
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping("/assignee/{assigneeId}/project/{projectId}")
    public String getTasksForAssignee(
            @PathVariable Long assigneeId,
            @PathVariable Long projectId,
            Model model) {

        // Получаем задачи для исполнителя в указанном проекте
        List<Task> tasks = taskService.getTasksByAssigneeAndProject(assigneeId, projectId);

        // Получаем информацию о пользователе и проекте для отображения в модели
        UserEntity assignee = userService.findById(assigneeId);
        Project project = projectService.findById(projectId);

        model.addAttribute("tasks", tasks);
        model.addAttribute("assignee", assignee);
        model.addAttribute("project", project);

        return "tasks"; // имя HTML-шаблона для отображения задач
    }


    // Метод для отображения страницы добавления задачи
    @GetMapping("/add/assignee/{assigneeId}/project/{projectId}")
    public String showAddTaskForm(@PathVariable Long assigneeId,
                                  @PathVariable Long projectId,
                                  Model model) {
        model.addAttribute("assignee", userService.findById(assigneeId));
        model.addAttribute("project", projectService.findById(projectId));
        return "add_task";
    }

    // Метод для добавления новой задачи
    @PostMapping("/add")
    public String addTask(@RequestParam Long assigneeId,
                          @RequestParam Long projectId,
                          @RequestParam String title,
                          @RequestParam(required = false) String description,
                          @RequestParam String status, // Получаем статус как String
                          @RequestParam LocalDate dueDate) {
        // Создаем новую задачу и сохраняем её
        Task newTask = Task.builder()
                .title(title)
                .description(description)
                .status(TaskStatus.valueOf(status)) // Преобразуем строку в TaskStatus
                .dueDate(dueDate)
                .build();

        // Устанавливаем проект и исполнителя
        newTask.setProject(projectService.findById(projectId));
        newTask.setAssignee(userService.findById(assigneeId));

        // Сохраняем задачу
        taskService.saveTask(newTask);

        // Перенаправляем на страницу задач исполнителяas
        return "redirect:/tasks/assignee/" + assigneeId + "/project/" + projectId;
    }

    @PostMapping("/{taskId}/updateDueDate")
    public String updateDueDate(@PathVariable Long taskId, @RequestParam("dueDate") LocalDate dueDate) {
        Task task = taskService.findById(taskId); // Найдите задачу по ID
        task.setDueDate(dueDate); // Обновите срок выполнения
        taskService.saveTask(task); // Сохраните изменения

        return "redirect:/tasks/assignee/" + task.getAssignee().getId() + "/project/" + task.getProject().getId();
    }
}
