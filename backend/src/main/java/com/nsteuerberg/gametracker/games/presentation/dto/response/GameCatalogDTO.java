package com.nsteuerberg.gametracker.games.presentation.dto.response;

import java.util.List;

public record GameCatalogDTO (
        Long id,
        String name,
        String coverUrl,
        List<String> platforms
){
}
