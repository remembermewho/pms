package com.PSM.demo.repository;

import com.PSM.demo.model.Project;
import com.PSM.demo.model.Task;
import com.PSM.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Дополнительные методы запросов могут быть определены здесь
    List<Task> findByAssignee(UserEntity assignee);
    List<Task> findByAssigneeIdAndProjectId(Long assigneeId, Long projectId);
}
