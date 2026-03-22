package com.nsteuerberg.gametracker.games.helpers;

import com.nsteuerberg.gametracker.games.persistance.entity.GenreEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.PlatformEntity;
import com.nsteuerberg.gametracker.igdb.dto.CommonDTO;
import com.nsteuerberg.gametracker.igdb.dto.CoverDTO;
import com.nsteuerberg.gametracker.igdb.dto.IgdbGameDTO;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

public class GameTestHelpers {
    public static final Instant LAST_SYNC = Instant.MAX;
    public static final String GAME_NAME = "Test";

    public static PlatformEntity createTestPlatform() {
        return new PlatformEntity(1L, 20L, "ds", "Nintendo DS", new HashSet<>());
    }

    public static GenreEntity createTestGenre() {
        return new GenreEntity(1L, 1L, "action", "Action", new HashSet<>());
    }

    public static IgdbGameDTO createValidTestGameDto() {
        return new IgdbGameDTO(1L, GAME_NAME, "test", "test", "", 4.0, 1000,
                List.of(new CommonDTO(20L, "ds", "Nintendo DS")), List.of(new CommonDTO(1L, "action", "Action")),
                Instant.MIN.getEpochSecond(), Instant.MIN.getEpochSecond(), new CoverDTO(1L, "//t_thumb.jpg"), List.of(),
                List.of()
        );
    }

    public static IgdbGameDTO createInvalidTestGameDtoByPlatform() {
        return new IgdbGameDTO(1L, GAME_NAME, "test", "test", "", 4.0, 1000,
                null, List.of(new CommonDTO(1L, "action", "Action")),
                Instant.MIN.getEpochSecond(), Instant.MIN.getEpochSecond(), new CoverDTO(1L, "//t_thumb.jpg"), List.of(),
                List.of()
        );
    }

    public static IgdbGameDTO createInvalidTestGameDtoByGenre() {
        return new IgdbGameDTO(1L, GAME_NAME, "test", "test", "", 4.0, 1000,
                List.of(new CommonDTO(20L, "ds", "Nintendo DS")), null,
                Instant.MIN.getEpochSecond(), Instant.MIN.getEpochSecond(), new CoverDTO(1L, "//t_thumb.jpg"), List.of(),
                List.of()
        );
    }

    public static IgdbGameDTO createInvalidTestGameDtoByCover() {
        return new IgdbGameDTO(1L, GAME_NAME, "test", "test", "", 4.0, 1000,
                List.of(new CommonDTO(20L, "ds", "Nintendo DS")), List.of(new CommonDTO(1L, "action", "Action")),
                Instant.MIN.getEpochSecond(), Instant.MIN.getEpochSecond(), null, List.of(),
                List.of()
        );
    }

    public static IgdbGameDTO createInvalidTestGameDtoByReleaseDate() {
        return new IgdbGameDTO(1L, GAME_NAME, "test", "test", "", 4.0, 1000,
                List.of(new CommonDTO(20L, "ds", "Nintendo DS")), List.of(new CommonDTO(1L, "action", "Action")),
                null, Instant.MIN.getEpochSecond(), new CoverDTO(1L, "//t_thumb.jpg"), List.of(),
                List.of()
        );
    }
}
