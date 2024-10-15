package com.PSM.demo.repository;

import com.PSM.demo.model.Project;
import com.PSM.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Дополнительные методы запросов могут быть определены здесь
    List<Project> findByManager(UserEntity manager);

    @Query("SELECT p FROM Project p JOIN p.assignedUsers u WHERE u = :user")
    List<Project> findByAssignedUsers(@Param("user") UserEntity user);
}
