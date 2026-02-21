package com.nsteuerberg.gametracker.games.service.exceptions;

import com.nsteuerberg.gametracker.shared.exceptions.ResourceNotFoundException;

public class GameNotFoundException extends ResourceNotFoundException {
    public GameNotFoundException(String gameSlug) {
        super(String.format("El juego %d no existe en el catalogo", gameSlug));
    }
    public GameNotFoundException(Long id) {
        super(String.format("El juego %d no existe en el catalogo", id));
    }
}
