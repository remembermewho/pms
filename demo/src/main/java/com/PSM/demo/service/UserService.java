package com.PSM.demo.service;

import com.PSM.demo.dto.UserAuthDTO;
import com.PSM.demo.dto.UserDTO;
import com.PSM.demo.dto.UserRegistrationDTO;
import com.PSM.demo.model.Role;
import com.PSM.demo.model.UserEntity;
import com.PSM.demo.repository.RoleRepository;
import com.PSM.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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


    public void convertToUserDTO(UserEntity user) {
        UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName())
                .build();
    }

    public UserEntity convertToUserEntity(UserRegistrationDTO registrationDTO, Role role) {
        return UserEntity.builder()
                .username(registrationDTO.getUsername())
                .password(registrationDTO.getPassword()) // Пароль зашифруем позже
                .email(registrationDTO.getEmail())
                .role(role)
                .build();
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Метод для получения пользователя по имени
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public UserEntity findById(Long managerId) {
        return userRepository.findById(managerId).orElseThrow(()->
                new UsernameNotFoundException("Manager not found"));
    }


    public List<UserEntity> findUsersByIds(List<Long> ids) {
        return userRepository.findAllById(ids);  // Стандартный метод JPA для поиска по списку ID
    }
}
