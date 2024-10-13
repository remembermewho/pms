package com.PSM.demo.dto;
import lombok.*;
/*
    Этот DTO будет использоваться для ответа клиенту с информацией о пользователе.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String roleName;
}
