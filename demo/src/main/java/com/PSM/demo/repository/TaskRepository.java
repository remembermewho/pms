package com.PSM.demo.repository;

import com.PSM.demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Дополнительные методы запросов могут быть определены здесь
}
