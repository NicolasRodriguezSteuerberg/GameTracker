package com.nsteuerberg.gametracker.games.persistance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "platforms")
public class PlatformEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "igdb_id", unique = true, nullable = false)
    private Long igdbId;
    @Column(unique = true, nullable = false)
    private String name;
    private String slug;
    @ManyToMany(mappedBy = "platforms")
    private Set<GameEntity> games = new HashSet<>();

    @Override
    public String toString() {
        return "PlatformEntity{" +
                "name='" + name + '\'' +
                ", igdbId=" + igdbId +
                ", id=" + id +
                '}';
    }
}
