package com.nsteuerberg.gametracker.games.presentation.mapper;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.games.presentation.dto.response.GameCatalogDTO;
import com.nsteuerberg.gametracker.games.presentation.dto.response.PageDTO;
import org.springframework.data.domain.Page;

public class GameMapper {
    public static PageDTO toPageCatalogDTO(Page<GameEntity> gamePage) {
        return PageDTO.builder()
                .pageNumber(gamePage.getNumber())
                .pageSize(gamePage.getSize())
                .totalElements(gamePage.getNumberOfElements())
                .totalPages(gamePage.getTotalPages())
                .last(gamePage.isLast())
                .content(gamePage.getContent().stream()
                        .map(game ->
                                new GameCatalogDTO(
                                        game.getId(),
                                        game.getName(),
                                        "https:" + game.getCoverUrl(),
                                        game.getPlatforms().stream()
                                                .map(p -> p.getName())
                                                .toList()
                                )
                        )
                        .toList()
                )
                .build();
    }
}
