package com.PSM.demo.service;

import com.PSM.demo.enums.TaskStatus;
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

    public List<Task> findTasksByAssignee(UserEntity assignee) {
        return taskRepository.findByAssignee(assignee);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public void updateTask(Long taskId, Task task) {
        task.setId(taskId);
        taskRepository.save(task);
    }

    public List<Task> getTasksByAssigneeAndProject(Long assigneeId, Long projectId) {
        return taskRepository.findByAssigneeIdAndProjectId(assigneeId, projectId);
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // Метод для обновления срока выполнения задачи и автоматического изменения статуса
    public void updateDueDateAndStatus(Long taskId, LocalDate newDueDate) {
        Task task = findById(taskId);
        task.setDueDate(newDueDate);

        // Автоматически обновляем статус задачи в зависимости от новой даты
        updateStatusBasedOnDate(task);

        // Сохраняем изменения в базе данных
        taskRepository.save(task);
    }

    // Метод, который обновляет статус задачи в зависимости от текущей даты и конечной даты
    private void updateStatusBasedOnDate(Task task) {
        LocalDate today = LocalDate.now();
        if (today.isAfter(task.getDueDate())) {
            task.setStatus(TaskStatus.COMPLETED);
        } else if (today.isEqual(task.getDueDate())) {
            task.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            task.setStatus(TaskStatus.NOT_STARTED);
        }
    }

    // Метод для явного обновления статуса задачи (при необходимости вручную)
    public void updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = findById(taskId);
        task.setStatus(newStatus);
        taskRepository.save(task);
    }
}
