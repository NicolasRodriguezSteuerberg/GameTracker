package com.nsteuerberg.gametracker.games.service.exceptions;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String gameSlug) {
        super(String.format("El juego %d no existe en el catalogo", gameSlug));
    }
}
