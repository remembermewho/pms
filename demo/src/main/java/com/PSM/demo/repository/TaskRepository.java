package com.PSM.demo.repository;

import com.PSM.demo.enums.TaskStatus;
import com.PSM.demo.model.Project;
import com.PSM.demo.model.Task;
import com.PSM.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Задачи, назначенные конкретному исполнителю
    List<Task> findByAssignee(UserEntity assignee);

    // Задачи для конкретного исполнителя в рамках проекта
    List<Task> findByAssigneeIdAndProjectId(Long assigneeId, Long projectId);

    // Задачи для конкретного проекта
    List<Task> findByProject(Project project);

    // Задачи с определенным статусом
    List<Task> findByStatus(TaskStatus status);

    // Задачи, у которых дата начала после заданной
    List<Task> findByStartDateAfter(LocalDate startDate);

    // Задачи, у которых дата завершения до заданной
    List<Task> findByDueDateBefore(LocalDate dueDate);

    // Задачи с определенным статусом и датой начала позже заданной
    List<Task> findByStatusAndStartDateAfter(TaskStatus status, LocalDate startDate);

    // Задачи, содержащие текст в названии
    @Query("SELECT t FROM Task t WHERE :title IS NULL OR LOWER(t.title) LIKE LOWER(:title)")
    List<Task> findTasksByTitle(@Param("title") String title);

    /**
     * Поиск задач по исполнителю, проекту и названию (включает фильтрацию).
     * Используется для комбинаций критериев.
     */
    @Query("SELECT t FROM Task t " +
            "WHERE (:assigneeId IS NULL OR t.assignee.id = :assigneeId) " +
            "AND (:projectId IS NULL OR t.project.id = :projectId) " +
            "AND (:title IS NULL OR LOWER(t.title) LIKE LOWER(:title))")
    List<Task> findTasksByAssigneeProjectAndTitle(
            @Param("assigneeId") Long assigneeId,
            @Param("projectId") Long projectId,
            @Param("title") String title);

    /**
     * Поиск задач по различным критериям: статус, даты и т.д.
     * Все параметры необязательны.
     */
    @Query("SELECT t FROM Task t " +
            "WHERE (:status IS NULL OR t.status = :status) " +
            "AND (:startDate IS NULL OR t.startDate >= :startDate) " +
            "AND (:dueDate IS NULL OR t.dueDate <= :dueDate) " +
            "AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId) " +
            "AND (:projectId IS NULL OR t.project.id = :projectId) " +
            "AND (:title IS NULL OR LOWER(t.title) LIKE LOWER(:title))")
    List<Task> searchTasksByMultipleCriteria(
            @Param("status") TaskStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("dueDate") LocalDate dueDate,
            @Param("assigneeId") Long assigneeId,
            @Param("projectId") Long projectId,
            @Param("title") String title);

}
