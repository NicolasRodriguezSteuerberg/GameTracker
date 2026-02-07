package com.nsteuerberg.gametracker.library.service.exceptions;

import com.nsteuerberg.gametracker.shared.exceptions.EntityConflictException;

public class GameAlreadyInLibraryException extends EntityConflictException {
    public GameAlreadyInLibraryException(Long userId, Long gameId) {
        super("LibraryEntry", "gameUserId", userId + "-" + gameId);
    }
}
