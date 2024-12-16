package com.PSM.demo.model;

import com.PSM.demo.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status; // Статус задачи (не начата, выполняется, завершена, отложена)

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate; // Дата начала задачи

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate; // Дата окончания задачи

    @Column(name = "reason_for_deadline_change", length = 500)
    private String reasonForDeadlineChange; // Причина изменения сроков

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project; // Проект, к которому принадлежит задача

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id", referencedColumnName = "id", nullable = false)
    private UserEntity assignee; // Исполнитель задачи

    @Column(name = "change_reason")
    private String changeReason; // Причина изменения сроков

    @Column(name = "is_completed") // Используем объектный тип Boolean
    private Boolean isCompleted; // Статус выполнения задачи
}
