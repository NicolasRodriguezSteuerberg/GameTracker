package com.nsteuerberg.gametracker.auth.persistance.entity;

import com.nsteuerberg.gametracker.auth.utils.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    @Column(name = "created_at")
    private Instant createdAt;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<RefreshTokenEntity> refreshTokens;
}
