package com.nsteuerberg.gametracker.library.presentation.dto.response;

import com.nsteuerberg.gametracker.games.presentation.dto.response.GameCatalogDTO;
import com.nsteuerberg.gametracker.library.persistance.entity.LibraryEntryEntity;
import com.nsteuerberg.gametracker.library.utils.GameStatus;

import java.time.Instant;

public record LibraryGameDTO (
        GameCatalogDTO game,
        GameStatus status,
        Double score,
        Integer playedMinutes,
        Instant addedAt
){
    public static LibraryGameDTO fromEntity(LibraryEntryEntity libraryEntry) {
        return new LibraryGameDTO(
                GameCatalogDTO.fromEntity(libraryEntry.getGame()),
                libraryEntry.getStatus(),
                libraryEntry.getScore(),
                libraryEntry.getPlaytimeMinutes(),
                libraryEntry.getAddedAt()
        );
    }
}
