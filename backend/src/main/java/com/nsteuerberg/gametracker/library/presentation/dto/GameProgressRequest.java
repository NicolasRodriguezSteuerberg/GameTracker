package com.nsteuerberg.gametracker.library.presentation.dto;

import com.nsteuerberg.gametracker.library.utils.GameStatus;

public record GameProgressRequest(
    GameStatus status,
    Double score,
    Integer playtimeMinutes
){
}
