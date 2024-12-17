package com.PSM.demo.service;

import com.PSM.demo.model.PersonalTask;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.repository.PersonalTaskRepository;
import com.PSM.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PersonalTaskService {

    private final PersonalTaskRepository personalTaskRepository;
    private final UserRepository userRepository;

    public PersonalTaskService(PersonalTaskRepository personalTaskRepository, UserRepository userRepository) {
        this.personalTaskRepository = personalTaskRepository;
        this.userRepository = userRepository;
    }

    public PersonalTask createTask(String username, String title, String description) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        PersonalTask task = PersonalTask.builder()
                .title(title)
                .description(description)
                .completed(false)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        return personalTaskRepository.save(task);
    }

    public List<PersonalTask> getTasksByUser(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
        return user.getPersonalTasks();
    }

    public void deleteTaskById(Long taskId) {
        personalTaskRepository.deleteById(taskId);
    }
}
