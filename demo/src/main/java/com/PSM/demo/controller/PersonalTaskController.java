package com.PSM.demo.controller;

import com.PSM.demo.model.PersonalTask;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.ui.Model;
import com.PSM.demo.model.Task;
import com.PSM.demo.service.PersonalTaskService;
import com.PSM.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequestMapping("/personalTasks")
@RequiredArgsConstructor
public class PersonalTaskController {
    private final PersonalTaskService personalTaskService;
    private final UserService userService;

    // Отображение страницы задач
    @GetMapping
    public String showTasks(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Получаем текущего пользователя
        String username = userDetails.getUsername();
        List<PersonalTask> tasks = personalTaskService.getTasksByUser(username);

        model.addAttribute("tasks", tasks); // Передаем список задач в модель
        model.addAttribute("newTask", new Task()); // Пустой объект для формы добавления

        return "personalTasks"; // Шаблон personalTasks.html
    }

    // Добавление новой задачи
    @PostMapping("/add")
    public String addTask(
            @ModelAttribute("newTask") Task newTask,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Получаем текущего пользователя
        String username = userDetails.getUsername();

        // Добавляем задачу для пользователя
        personalTaskService.createTask(username, newTask.getTitle(), newTask.getDescription());

        // Перенаправляем обратно на страницу задач
        return "redirect:/personalTasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        personalTaskService.deleteTaskById(id);
        return "redirect:/personalTasks";
    }
}
