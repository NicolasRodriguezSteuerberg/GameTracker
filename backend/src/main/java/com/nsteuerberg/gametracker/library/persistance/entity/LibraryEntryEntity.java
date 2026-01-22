package com.nsteuerberg.gametracker.library.persistance.entity;

import com.nsteuerberg.gametracker.auth.persistance.entity.UserEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.library.utils.GameStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "library_entries")
public class LibraryEntryEntity {
    @EmbeddedId
    private GameUserId id;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id")
    private GameEntity game;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private GameStatus status;
    @Column()
    private Double score;
    @Column(name="playtime_minutes")
    private Integer playtimeMinutes;

    private Instant addedAt = Instant.now();
    private Instant lastUpdated = Instant.now();
}
