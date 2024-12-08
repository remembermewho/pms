package com.PSM.demo.service;

import com.PSM.demo.enums.TaskStatus;
import com.PSM.demo.model.Project;
import com.PSM.demo.model.Task;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Получаем список задач, назначенных определенному исполнителю
    public List<Task> findTasksByAssignee(UserEntity assignee) {
        return taskRepository.findByAssignee(assignee);
    }

    // Получаем задачи, назначенные конкретному исполнителю в рамках проекта
    public List<Task> getTasksByAssigneeAndProject(Long assigneeId, Long projectId) {
        return taskRepository.findByAssigneeIdAndProjectId(assigneeId, projectId);
    }

    // Получаем задачу по ID
    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    // Сохраняем новую задачу
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    // Обновление задачи по ID, включая автоматическое обновление статуса на основе дат
    public void updateTask(Long taskId, Task task) {
        task.setId(taskId);
        updateStatusBasedOnDates(task); // Обновляем статус в зависимости от дат
        taskRepository.save(task);
    }

    // Метод для обновления дат начала и завершения задачи и автоматического изменения статуса
    public void updateDatesAndStatus(Long taskId, LocalDate newStartDate, LocalDate newDueDate) {
        Task task = findById(taskId);
        task.setStartDate(newStartDate);
        task.setDueDate(newDueDate);

        // Обновляем статус задачи на основе новых дат
        updateStatusBasedOnDates(task);

        // Сохраняем изменения в базе данных
        taskRepository.save(task);
    }

    // Метод для явного обновления статуса задачи (при необходимости вручную)
    public void updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = findById(taskId);
        task.setStatus(newStatus);
        taskRepository.save(task);
    }

    // Получить список задач для конкретного проекта
    public List<Task> getTasksByProject(Project project) {
        return taskRepository.findByProject(project);
    }

    // Метод для обновления статуса задачи на основе дат начала, завершения и текущей даты
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

    // Найти задачу по ID с обработкой исключений, если задача не найдена
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
}
