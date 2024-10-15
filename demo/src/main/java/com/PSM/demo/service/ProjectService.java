package com.PSM.demo.service;

import com.PSM.demo.model.Project;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.repository.ProjectRepository;
import com.PSM.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));
    }

    public void updateProject(Long projectId, Project project) {
        Project existingProject = findProjectById(projectId);
        existingProject.setName(project.getName());
        existingProject.setDescription(project.getDescription());
        existingProject.setStartDate(project.getStartDate());
        existingProject.setEndDate(project.getEndDate());
        projectRepository.save(existingProject);
    }

    public void addAssigneeToProject(Long projectId, Long userId) {
        Project project = findProjectById(projectId);
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        userOpt.ifPresent(project::addAssignedUser);
        projectRepository.save(project);
    }

    public void removeAssigneeFromProject(Project project, UserEntity user) {
        // Удаление пользователя из списка исполнителей
        project.getAssignedUsers().remove(user);
        // Сохранение проекта после изменений
        projectRepository.save(project);
    }

    public List<Project> findByManager(UserEntity manager) {
        return projectRepository.findByManager(manager);
    }

    public List<Project> findByAssignedUser(UserEntity user) {
        return projectRepository.findByAssignedUsers(user);
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Проект не найден."));
    }

    public void addAssigneesToProject(Project project, List<UserEntity> assignees) {
        project.getAssignedUsers().addAll(assignees);  // Добавляем всех исполнителей к проекту
        projectRepository.save(project);  // Сохраняем изменения в базе данных
    }
}
