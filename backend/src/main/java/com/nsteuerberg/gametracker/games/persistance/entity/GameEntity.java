package com.nsteuerberg.gametracker.games.persistance.entity;

import com.nsteuerberg.gametracker.games.persistance.entity.embed.GameTimeData;
import com.nsteuerberg.gametracker.games.persistance.entity.embed.ScoreData;
import com.nsteuerberg.gametracker.games.persistance.entity.embed.VideoData;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "games")
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, name = "igdb_id")
    private Long igdbId;

    @Column(nullable = false, unique = true)
    private String slug;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String summary;
    @Column(columnDefinition = "TEXT")
    private String storyline;

    @Embedded // Se recoge en grupo, se guarda como columnas diferentes
    private ScoreData score;

    //@Embedded
    //private GameTimeData timeToBeat;

    @Column(name = "cover_url")
    private String coverUrl;

    // crea tabla auxiliar 1-n
    @ElementCollection()
    @CollectionTable(
            name = "game_videos",
            joinColumns = @JoinColumn(name = "game_id")
    )
    private List<VideoData> videos = new ArrayList<>();

    @ElementCollection()
    @CollectionTable(
            name = "game_screenshots",
            joinColumns = @JoinColumn(name = "game_id")
    )
    @Column(name = "image_url") // nombre en la tabla de screenshots
    private List<String> screenshots = new ArrayList<>();

    @Column(name = "first_release_date")
    private Instant firstReleaseDate;
    @Column(name = "last_updated")
    private Instant lastUpdated;

    @ManyToMany()
    @JoinTable(
            name = "game_genres",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<GenreEntity> genres = new HashSet<>();
    @ManyToMany()
    @JoinTable(
            name = "game_platforms",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private Set<PlatformEntity> platforms = new HashSet<>();

    @Override
    public String toString() {
        return "GameEntity{" +
                "id=" + id +
                ", igdbId=" + igdbId +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", firstReleaseDate=" + firstReleaseDate +
                ", genres=" + genres +
                ", platforms=" + platforms +
                '}';
    }
}
