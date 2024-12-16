package com.PSM.demo.controller;

import com.PSM.demo.enums.TaskStatus;
import com.PSM.demo.model.Project;
import com.PSM.demo.model.Task;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.service.ProjectService;
import com.PSM.demo.service.TaskService;
import com.PSM.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(value = "search", required = false) String searchTerm,
            Model model) {

        List<Task> tasks;
        if (searchTerm == null || searchTerm.isBlank()) {
            tasks = taskService.getTasksByAssigneeAndProject(assigneeId, projectId);
        } else {
            tasks = taskService.searchTasksByAssigneeAndProject(assigneeId, projectId, searchTerm);
        }

        UserEntity assignee = userService.findById(assigneeId);
        Project project = projectService.findById(projectId);

        model.addAttribute("tasks", tasks);
        model.addAttribute("assignee", assignee);
        model.addAttribute("project", project);
        model.addAttribute("searchTerm", searchTerm);

        return "tasks"; // Имя Thymeleaf-шаблона для отображения задач
    }

    @GetMapping("/add/assignee/{assigneeId}/project/{projectId}")
    public String showAddTaskForm(@PathVariable Long assigneeId,
                                  @PathVariable Long projectId,
                                  Model model) {
        model.addAttribute("assignee", userService.findById(assigneeId));
        model.addAttribute("project", projectService.findById(projectId));
        return "add_task";
    }

    @PostMapping("/add")
    public String addTask(@RequestParam Long assigneeId,
                          @RequestParam Long projectId,
                          @RequestParam String title,
                          @RequestParam(required = false) String description,
                          @RequestParam String status,
                          @RequestParam LocalDate startDate,
                          @RequestParam LocalDate dueDate) {

        Task newTask = Task.builder()
                .title(title)
                .description(description)
                .status(TaskStatus.valueOf(status))
                .startDate(startDate)
                .dueDate(dueDate)
                .build();

        newTask.setProject(projectService.findById(projectId));
        newTask.setAssignee(userService.findById(assigneeId));

        taskService.saveTask(newTask);

        return "redirect:/tasks/assignee/" + assigneeId + "/project/" + projectId;
    }

    @PostMapping("/{taskId}/updateDates")
    public String updateDatesWithReason(@PathVariable Long taskId,
                                        @RequestParam("startDate") LocalDate startDate,
                                        @RequestParam("dueDate") LocalDate dueDate,
                                        @RequestParam("reason") String reason) {
        taskService.updateDatesAndReason(taskId, startDate, dueDate, reason);

        Task task = taskService.findById(taskId);
        return "redirect:/tasks/assignee/" + task.getAssignee().getId() + "/project/" + task.getProject().getId();
    }

    @PostMapping("/{taskId}/updateCompletionStatus")
    public String updateCompletionStatus(@PathVariable Long taskId,
                                         @RequestParam("isCompleted") boolean isCompleted) {
        taskService.updateTaskCompletionStatus(taskId, isCompleted);

        Task task = taskService.findById(taskId);
        return "redirect:/tasks/assignee/" + task.getAssignee().getId() + "/project/" + task.getProject().getId();
    }
}
