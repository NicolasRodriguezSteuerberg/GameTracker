package com.nsteuerberg.gametracker.games.helpers;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.embed.ScoreData;
import com.nsteuerberg.gametracker.games.persistance.entity.embed.VideoData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static com.nsteuerberg.gametracker.games.helpers.SyncUpdateGamesTestHelpers.createTestGenre;
import static com.nsteuerberg.gametracker.games.helpers.SyncUpdateGamesTestHelpers.createTestPlatform;

public class GameServiceTestHelpers {
    public static final String GAME_SLUG = "elden-ring";

    public static GameEntity createGameEntity() {
        return GameEntity.builder()
                .id(1L)
                .slug(GAME_SLUG)
                .name("Elden Ring")
                .summary("Summary")
                .storyline("Storyline")
                .score(new ScoreData(4.0, 1000))
                .coverUrl("https://t_cover_big.jpg")
                .videos(List.of(new VideoData("123124", "trailer")))
                .screenshots(List.of())
                .firstReleaseDate(Instant.MIN)
                .lastUpdated(Instant.MAX)
                .genres(Set.of(createTestGenre()))
                .platforms(Set.of(createTestPlatform(), createTestPlatform()))
                .build();
    }

    public static Pageable getPageable() {
        Pageable pageable = Pageable.ofSize(1);
        pageable.first();
        return pageable;
    }

    public static Page<GameEntity> getPageGames(Pageable pageable) {
        return new PageImpl<GameEntity>(List.of(createGameEntity()), pageable, 1);
    }
}
