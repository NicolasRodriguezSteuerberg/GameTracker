package com.nsteuerberg.gametracker.library.service.exceptions;

public class GameNotFoundInLibraryException extends RuntimeException {
    public GameNotFoundInLibraryException(Long userId, Long gameId) {
        super(String.format("El juego %d no se encuentra en la biblioteca del usuario %d", userId, gameId));
    }
}
