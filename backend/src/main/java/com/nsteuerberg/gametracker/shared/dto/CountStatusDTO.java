package com.nsteuerberg.gametracker.shared.dto;

import com.nsteuerberg.gametracker.library.utils.GameStatus;

public record CountStatusDTO (
        GameStatus value,
        Long count
){
}
