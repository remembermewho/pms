package com.PSM.demo.dto;
import lombok.*;

/*
    Этот DTO для передачи информации о роли.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDTO {
    private Long id;
    private String roleName;
}
