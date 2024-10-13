package com.PSM.demo.dto;
import lombok.*;

/*
    Этот DTO будет использоваться для передачи информации
    о пользователе (например, при запросах).
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String roleName;
}
