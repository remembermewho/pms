package com.PSM.demo.repository;

import com.PSM.demo.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Дополнительные методы запросов могут быть определены здесь
}
