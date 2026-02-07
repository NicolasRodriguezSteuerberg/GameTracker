package com.nsteuerberg.gametracker.library.presentation.dto.request;

import com.nsteuerberg.gametracker.library.utils.GameStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

public record GameUpdateRequest (
    GameStatus status,
    @Min(0) @Max(10)
    Double score,
    @PositiveOrZero
    Integer playtimeMinutes
){
    public GameUpdateRequest {
        if (status == null && score == null && playtimeMinutes == null) {
            throw new IllegalArgumentException("Al menos un campo debe ser enviado");
        }
    }
}
