package com.nsteuerberg.gametracker.games.presentation.dto.response;

import java.time.Instant;

public record GameDTO (
    Long id,
    String coverUrl,
    String title,
    String summary,
    Instant releaseDate
){
}
