package com.PSM.demo.dto;
import lombok.*;
/*
    Этот DTO будет использоваться для регистрации пользователя.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDTO {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String roleName; // Добавлено поле для роли
}
