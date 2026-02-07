package com.nsteuerberg.gametracker.library.service;

import com.nsteuerberg.gametracker.auth.persistance.repository.UserRepository;
import com.nsteuerberg.gametracker.games.persistance.repository.GameRepository;
import com.nsteuerberg.gametracker.library.presentation.mapper.LibraryMapper;
import com.nsteuerberg.gametracker.shared.dto.FilterDTO;
import com.nsteuerberg.gametracker.shared.dto.PageDTO;
import com.nsteuerberg.gametracker.library.persistance.entity.GameUserId;
import com.nsteuerberg.gametracker.library.persistance.entity.LibraryEntryEntity;
import com.nsteuerberg.gametracker.library.persistance.repository.LibraryEntryRepository;
import com.nsteuerberg.gametracker.library.persistance.repository.LibrarySpecification;
import com.nsteuerberg.gametracker.library.presentation.dto.request.GameAddRequest;
import com.nsteuerberg.gametracker.library.presentation.dto.request.GameUpdateRequest;
import com.nsteuerberg.gametracker.library.presentation.dto.response.LibraryGameDTO;
import com.nsteuerberg.gametracker.library.service.exceptions.GameAlreadyInLibraryException;
import com.nsteuerberg.gametracker.library.service.exceptions.GameNotFoundInLibraryException;
import com.nsteuerberg.gametracker.library.utils.GameStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserLibraryService {
    private final LibraryEntryRepository libraryEntryRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    private final static Logger log = LoggerFactory.getLogger(UserLibraryService.class);

    public PageDTO getLibrary(Long userId, Set<GameStatus> statuses, Set<Long> platforms, Set<Long> genres, String title, Pageable pageable) {
        Specification<LibraryEntryEntity> spec = Specification
                .where(LibrarySpecification.hasUserId(userId))
                .and(LibrarySpecification.withGameTitleLike(title))
                .and(LibrarySpecification.withGenres(genres))
                .and(LibrarySpecification.withStatuses(statuses))
                .and(LibrarySpecification.withPlatforms(platforms))
                .and(LibrarySpecification.fetchGameDetails());
        Page<LibraryEntryEntity> pageLibrary = libraryEntryRepository.findAll(spec, pageable);
        return LibraryMapper.toPageLibraryDTO(pageLibrary);
    }

    public LibraryGameDTO addGame(Long userId, Long gameId, GameAddRequest progressRequest) {
        log.debug("Intentando agregar el juego {} a la biblioteca del usuario {}", gameId, userId);
        GameUserId gameUserId = new GameUserId(userId, gameId);
        if (libraryEntryRepository.existsById(gameUserId)) {
            throw new GameAlreadyInLibraryException(userId, gameId);
        }
        LibraryEntryEntity saved = libraryEntryRepository.save(
                LibraryEntryEntity.builder()
                        .id(gameUserId)
                        .user(userRepository.getReferenceById(userId))
                        .game(gameRepository.getReferenceById(gameId))
                        .score(progressRequest.score())
                        .status(progressRequest.status())
                        .playtimeMinutes(progressRequest.playtimeMinutes())
                        .build()
        );
        log.info("Juego {} añadido con éxito a la biblioteca del usuario {}", gameId, userId);
        return LibraryGameDTO.fromEntity(saved);
    }

    public LibraryGameDTO updateGame(Long userId, Long gameId, GameUpdateRequest updateRequest) {
        log.debug("Intentando modificar el juego {} de la biblioteca del usuario {}");
        LibraryEntryEntity userGame = libraryEntryRepository.findById(new GameUserId(userId, gameId))
                .orElseThrow(() -> new GameNotFoundInLibraryException(userId, gameId));
        if (updateRequest.status() != null) {
            userGame.setStatus(updateRequest.status());
        }
        if (updateRequest.playtimeMinutes() != null) {
            userGame.setPlaytimeMinutes(updateRequest.playtimeMinutes());
        }
        if (updateRequest.score() != null) {
            userGame.setScore(userGame.getScore());
        }
        LibraryEntryEntity updatedGame = libraryEntryRepository.save(userGame);
        log.info("Juego {} modificado con éxito en la biblioteca del usuario {}", gameId, userId);
        return LibraryGameDTO.fromEntity(updatedGame);
    }

    public void deleteGame(Long userId, Long gameId) {
        log.debug("Intentando eliminar el juego {] de la biblioteca del usuario {}", gameId, userId);
        LibraryEntryEntity gameUser = libraryEntryRepository.findById(new GameUserId(userId, gameId))
                .orElseThrow(() -> new GameNotFoundInLibraryException(userId, gameId));
        libraryEntryRepository.delete(gameUser);
        log.info("Eliminado correctamente el juego {} de la biblioteca del usuario {}", gameId, userId);
    }

    public FilterDTO getFilters(Long userId) {
        return new FilterDTO(
                libraryEntryRepository.findPlatformsWithCount(userId),
                libraryEntryRepository.findGenresWithCount(userId),
                libraryEntryRepository.findStatusWithCount(userId)
        );
    }
}
