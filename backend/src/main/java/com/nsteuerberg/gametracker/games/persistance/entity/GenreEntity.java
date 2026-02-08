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
@Table(name = "genres")
public class GenreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "igdb_id", unique = true, nullable = false)
    private Long igdbId;
    @Column(unique = true, nullable = false)
    private String slug;
    @Column(nullable = false)
    private String name;
    @ManyToMany(mappedBy = "genres")
    private Set<GameEntity> games = new HashSet<>();

    @Override
    public String toString() {
        return "GenreEntity{" +
                "name='" + name + '\'' +
                ", igdbId=" + igdbId +
                ", id=" + id +
                '}';
    }
}
