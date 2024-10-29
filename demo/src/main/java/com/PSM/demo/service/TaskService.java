package com.PSM.demo.service;

import com.PSM.demo.model.Project;
import com.PSM.demo.model.Task;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private TaskRepository taskRepository;

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
        task.setId(taskId); // Убедись, что ID сохраняется
        taskRepository.save(task);
    }

    public List<Task> getTasksByAssigneeAndProject(Long assigneeId, Long projectId) {
        // Получаем задачи по исполнителю и проекту
        return taskRepository.findByAssigneeIdAndProjectId(assigneeId, projectId);
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

}
