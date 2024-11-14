package com.PSM.demo.repository;

import com.PSM.demo.model.Project;
import com.PSM.demo.model.Task;
import com.PSM.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Задачи, назначенные определенному исполнителю
    List<Task> findByAssignee(UserEntity assignee);

    // Задачи для определенного исполнителя в рамках проекта
    List<Task> findByAssigneeIdAndProjectId(Long assigneeId, Long projectId);

    // Задачи для конкретного проекта
    List<Task> findByProject(Project project);
}
