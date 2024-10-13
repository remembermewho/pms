package com.PSM.demo.service;

import com.PSM.demo.dto.UserAuthDTO;
import com.PSM.demo.dto.UserDTO;
import com.PSM.demo.dto.UserRegistrationDTO;
import com.PSM.demo.model.Role;
import com.PSM.demo.model.User;
import com.PSM.demo.repository.RoleRepository;
import com.PSM.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Для шифрования паролей

    public void registerUser(UserRegistrationDTO registrationDTO) {
        // Проверка на существование пользователя с таким же именем или email
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            System.out.println("Пользователь с таким именем уже существует.");
            throw new RuntimeException("Пользователь с таким именем уже существует.");
        }
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            System.out.println("Пользователь с таким email уже существует.");
            throw new RuntimeException("Пользователь с таким email уже существует.");
        }
        if (!Objects.equals(registrationDTO.getPassword(), registrationDTO.getConfirmPassword())){
            System.out.println("Пароли не совпадают");
            throw new RuntimeException("Пароли не совпадают.");
        }
        // Поиск роли по имени
        Role role = roleRepository.findByRoleName(registrationDTO.getRoleName())
                .orElseThrow(() -> new RuntimeException("Роль не найдена"));

        // Создание нового пользователя
        User user = convertToUserEntity(registrationDTO, role);

        // Шифрование пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Сохранение пользователя
        User savedUser = userRepository.save(user);

        // Возврат DTO
        convertToUserDTO(savedUser);
    }

    public boolean authenticateUser(UserAuthDTO loginDTO) {
        // Поиск пользователя по имени
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден."));

        // Проверка пароля
        if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return true; // Успешная авторизация
        } else {
            throw new RuntimeException("Неверный пароль.");
        }
    }

    public void convertToUserDTO(User user) {
        UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName())
                .build();
    }

    public User convertToUserEntity(UserRegistrationDTO registrationDTO, Role role) {
        return User.builder()
                .username(registrationDTO.getUsername())
                .password(registrationDTO.getPassword()) // Пароль зашифруем позже
                .email(registrationDTO.getEmail())
                .role(role)
                .build();
    }
}
