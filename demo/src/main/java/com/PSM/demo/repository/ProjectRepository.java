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

    // Найти проекты, где менеджер - конкретный пользователь
    List<Project> findByManager(UserEntity manager);

    // Найти проекты, где пользователь - исполнитель
    @Query("SELECT p FROM Project p JOIN p.assignedUsers u WHERE u = :user")
    List<Project> findByAssignedUsers(@Param("user") UserEntity user);

    // Поиск проектов по менеджеру и части названия
    List<Project> findByManagerAndNameContainingIgnoreCase(UserEntity manager, String name);

    // Поиск проектов по исполнителю и части названия
    @Query("SELECT p FROM Project p JOIN p.assignedUsers u WHERE u = :user AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Project> findByAssignedUsersAndNameContainingIgnoreCase(@Param("user") UserEntity user, @Param("name") String name);
}
