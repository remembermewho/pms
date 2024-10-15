package com.PSM.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roleName", nullable = false, unique = true)
    private String roleName;

    // Обратная связь с User
    @OneToMany(mappedBy = "role")
    private List<UserEntity> users;
}
