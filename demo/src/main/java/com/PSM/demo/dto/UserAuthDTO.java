package com.PSM.demo.dto;
import lombok.*;
/*
    Этот DTO будет использоваться для входа пользователя.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthDTO {
    private String username;
    private String password;
}
