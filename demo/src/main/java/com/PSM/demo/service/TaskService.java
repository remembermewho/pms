package com.PSM.demo.service;

import com.PSM.demo.enums.TaskStatus;
import com.PSM.demo.model.Notification;
import com.PSM.demo.model.Project;
import com.PSM.demo.model.Task;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    @Autowired
    public TaskService(TaskRepository taskRepository, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
    }

    // Получение задач, связанных с исполнителем
    public List<Task> findTasksByAssignee(UserEntity assignee) {
        return taskRepository.findByAssignee(assignee);
    }

    // Получение задач для конкретного исполнителя в рамках конкретного проекта
    public List<Task> getTasksByAssigneeAndProject(Long assigneeId, Long projectId) {
        return taskRepository.findByAssigneeIdAndProjectId(assigneeId, projectId);
    }

    // Получение задачи по ID
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // Сохранение новой или обновленной задачи
    public void saveTask(Task task) {
        updateStatusBasedOnDates(task);
        taskRepository.save(task);

        // Если задача завершена, уведомляем менеджера проекта
        if (task.getStatus() == TaskStatus.COMPLETED) {
            notifyManagerOnTaskCompletion(task);
        }
    }

    // Обновление задачи
    public void updateTask(Long taskId, Task updatedTask) {
        Task existingTask = findById(taskId);
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStartDate(updatedTask.getStartDate());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setAssignee(updatedTask.getAssignee());
        existingTask.setProject(updatedTask.getProject());
        saveTask(existingTask);
    }

    // Обновление дат задачи с указанием причины
    public void updateDatesAndReason(Long taskId, LocalDate newStartDate, LocalDate newDueDate, String reason) {
        Task task = findById(taskId);
        task.setStartDate(newStartDate);
        task.setDueDate(newDueDate);
        task.setReasonForDeadlineChange(reason);
        saveTask(task);
    }

    // Обновление статуса задачи
    public void updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = findById(taskId);
        task.setStatus(newStatus);
        saveTask(task);
    }

    // Обновление статуса выполнения задачи
    public void updateTaskCompletionStatus(Long taskId, Boolean isCompleted) {
        Task task = findById(taskId);
        task.setIsCompleted(isCompleted);
        saveTask(task);
    }

    // Получение задач, связанных с проектом
    public List<Task> getTasksByProject(Project project) {
        return taskRepository.findByProject(project);
    }

    // Получение задач по статусу
    public List<Task> findTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    // Получение задач с датой начала позже заданной
    public List<Task> findTasksByStartDateAfter(LocalDate startDate) {
        return taskRepository.findByStartDateAfter(startDate);
    }

    // Получение задач с датой завершения до заданной
    public List<Task> findTasksByDueDateBefore(LocalDate dueDate) {
        return taskRepository.findByDueDateBefore(dueDate);
    }

    // Получение задач по статусу и дате начала позже заданной
    public List<Task> findTasksByStatusAndStartDateAfter(TaskStatus status, LocalDate startDate) {
        return taskRepository.findByStatusAndStartDateAfter(status, startDate);
    }

    // Поиск задач по названию
    public List<Task> searchTasksByTitle(String title) {
        String processedTitle = (title != null && !title.isBlank()) ? "%" + title + "%" : null;
        return taskRepository.findTasksByTitle(processedTitle);
    }

    // Поиск задач по исполнителю, проекту и названию
    public List<Task> searchTasksByAssigneeAndProject(Long assigneeId, Long projectId, String title) {
        String processedTitle = (title != null && !title.isBlank()) ? "%" + title + "%" : null;
        return taskRepository.findTasksByAssigneeProjectAndTitle(assigneeId, projectId, processedTitle);
    }

    // Установка статуса задачи на основе текущей даты
    private void updateStatusBasedOnDates(Task task) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(task.getStartDate())) {
            task.setStatus(TaskStatus.NOT_STARTED);
        } else if (today.isAfter(task.getDueDate())) {
            task.setStatus(TaskStatus.COMPLETED);
        } else {
            task.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    // Уведомление менеджера о завершении задачи
    private void notifyManagerOnTaskCompletion(Task task) {
        Project project = task.getProject();
        UserEntity manager = project.getManager(); // Предполагается, что проект имеет связь с менеджером
        if (manager != null) {
            String message = "Задача '" + task.getTitle() + "' в проекте '" + project.getName() + "' завершена.";
            notificationService.sendNotification(manager, message);
        }
    }
}
