package com.PSM.demo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserLoginDTO {
    private String username;
    private String password;
}
