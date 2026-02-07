package com.nsteuerberg.gametracker.library.presentation.dto.request;

import com.nsteuerberg.gametracker.library.utils.GameStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;


public record GameAddRequest(
    GameStatus status,
    @Min(value = 0, message = "La puntuacion debe ser positiva")
    @Max(value = 10, message = "La puntuacion no puede ser mayor a 10")
    Double score,
    @PositiveOrZero
    Integer playtimeMinutes
){
    public GameAddRequest {
        if (status == null) {
            status = GameStatus.PENDING;
        }
        if (score == null) {
            score = 0.0;
        }
        if (playtimeMinutes == null) {
            playtimeMinutes = 0;
        }
    }
}
