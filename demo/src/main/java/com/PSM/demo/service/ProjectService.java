package com.PSM.demo.service;

import com.PSM.demo.model.Project;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Найти проект по ID.
     *
     * @param id идентификатор проекта.
     * @return найденный проект.
     */
    public Project findById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Проект с id " + id + " не найден."));
    }

    /**
     * Найти проекты, которыми управляет пользователь.
     *
     * @param manager менеджер, управляющий проектами.
     * @return список проектов.
     */
    public List<Project> findByManager(UserEntity manager) {
        return projectRepository.findByManager(manager);
    }

    /**
     * Найти проекты, в которых пользователь является исполнителем.
     *
     * @param user пользователь-исполнитель.
     * @return список проектов.
     */
    public List<Project> findByAssignedUser(UserEntity user) {
        return projectRepository.findByAssignedUsers(user);
    }

    /**
     * Найти проекты, которыми управляет пользователь с поиском по названию.
     *
     * @param manager менеджер, управляющий проектами.
     * @param searchTerm строка поиска по названию.
     * @return отфильтрованный список проектов.
     */
    public List<Project> findManagedProjects(UserEntity manager, String searchTerm) {
        return (searchTerm == null || searchTerm.isBlank())
                ? projectRepository.findByManager(manager)
                : projectRepository.findByManagerAndNameContainingIgnoreCase(manager, searchTerm);
    }

    /**
     * Найти проекты, в которых пользователь является исполнителем с поиском по названию.
     *
     * @param user пользователь-исполнитель.
     * @param searchTerm строка поиска по названию.
     * @return отфильтрованный список проектов.
     */
    public List<Project> findAssignedProjects(UserEntity user, String searchTerm) {
        return (searchTerm == null || searchTerm.isBlank())
                ? projectRepository.findByAssignedUsers(user)
                : projectRepository.findByAssignedUsersAndNameContainingIgnoreCase(user, searchTerm);
    }

    /**
     * Сохранить проект.
     *
     * @param project объект проекта для сохранения.
     */
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    /**
     * Добавить исполнителей в проект.
     *
     * @param project проект.
     * @param assignees список пользователей-исполнителей.
     */
    public void addAssigneesToProject(Project project, List<UserEntity> assignees) {
        project.getAssignedUsers().addAll(assignees);
        projectRepository.save(project);
    }

    /**
     * Удалить исполнителя из проекта.
     *
     * @param project проект.
     * @param user пользователь-исполнитель.
     */
    public void removeAssigneeFromProject(Project project, UserEntity user) {
        project.getAssignedUsers().remove(user);
        projectRepository.save(project);
    }
}
