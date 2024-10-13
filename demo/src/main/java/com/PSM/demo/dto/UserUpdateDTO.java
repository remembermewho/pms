package com.PSM.demo.dto;
import lombok.*;
/*
    Этот DTO для обновления данных пользователя (например, изменение пароля или роли).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDTO {
    private String username;
    private String email;
    private String newPassword;
    private String roleName;
}
