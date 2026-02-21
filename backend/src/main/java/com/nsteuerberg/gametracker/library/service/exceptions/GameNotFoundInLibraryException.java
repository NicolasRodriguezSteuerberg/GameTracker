package com.nsteuerberg.gametracker.library.service.exceptions;

import com.nsteuerberg.gametracker.shared.exceptions.ResourceNotFoundException;

public class GameNotFoundInLibraryException extends ResourceNotFoundException {
    public GameNotFoundInLibraryException(Long userId, Long gameId) {
        super(String.format("El juego %d no se encuentra en la biblioteca del usuario %d", userId, gameId));
    }
}
