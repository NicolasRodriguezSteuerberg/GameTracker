package com.nsteuerberg.gametracker.games.presentation.dto.response;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.GenreEntity;

import java.util.List;

public record GameCatalogDTO (
        Long id,
        String name,
        String coverUrl,
        List<String> platforms,
        List<String> genres
){
    public static GameCatalogDTO fromEntity(GameEntity game) {
        return new GameCatalogDTO(
                game.getId(),
                game.getName(),
                game.getCoverUrl(),
                game.getPlatforms().stream()
                        .map(platfrom -> platfrom.getName())
                        .toList(),
                game.getGenres().stream()
                        .map(GenreEntity::getName)
                        .toList()
        );
    }
}
