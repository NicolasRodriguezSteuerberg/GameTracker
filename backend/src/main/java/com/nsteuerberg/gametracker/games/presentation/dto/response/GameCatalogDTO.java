package com.nsteuerberg.gametracker.games.presentation.dto.response;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;

import java.util.List;

public record GameCatalogDTO (
        Long id,
        String slug,
        String name,
        String coverUrl,
        ScoreDTO score,
        List<CommonDTO> platforms,
        List<CommonDTO> genres
){
    public static GameCatalogDTO fromEntity(GameEntity game) {
        return new GameCatalogDTO(
                game.getId(),
                game.getSlug(),
                game.getName(),
                game.getCoverUrl(),
                ScoreDTO.fromEntity(game.getScore()),
                CommonDTO.fromPlatformsEntity(game.getPlatforms()),
                CommonDTO.fromGenrseEntity(game.getGenres())

        );
    }
}
