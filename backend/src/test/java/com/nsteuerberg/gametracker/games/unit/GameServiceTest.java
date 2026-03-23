package com.nsteuerberg.gametracker.games.unit;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.games.persistance.repository.GameRepository;
import com.nsteuerberg.gametracker.games.persistance.repository.GenreRepository;
import com.nsteuerberg.gametracker.games.persistance.repository.PlatformRepository;
import com.nsteuerberg.gametracker.games.presentation.dto.response.GameCatalogDTO;
import com.nsteuerberg.gametracker.games.presentation.dto.response.GameDTO;
import com.nsteuerberg.gametracker.games.service.GameService;
import com.nsteuerberg.gametracker.games.service.exceptions.GameNotFoundException;
import com.nsteuerberg.gametracker.shared.dto.FilterDTO;
import com.nsteuerberg.gametracker.shared.dto.PageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static com.nsteuerberg.gametracker.games.helpers.GameServiceTestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    GameRepository gameRepository;
    @Mock
    GenreRepository genreRepository;
    @Mock
    PlatformRepository platformRepository;

    @InjectMocks
    GameService gameService;

    @Test
    void shouldReturnGameDTO_whenGameExists() {
        GameEntity gameEntity = createGameEntity();
        when(gameRepository.findBySlug(GAME_SLUG)).thenReturn(Optional.of(gameEntity));

        GameDTO game = gameService.getGame(GAME_SLUG);

        verify(gameRepository, times(1)).findBySlug(GAME_SLUG);

        // Campos directos
        assertEquals(gameEntity.getName(), game.title());
        assertEquals(gameEntity.getSummary(), game.summary());
        assertEquals(gameEntity.getStoryline(), game.storyline());
        assertEquals(gameEntity.getCoverUrl(), game.coverUrl());
        assertEquals(gameEntity.getFirstReleaseDate(), game.releaseDate());

        // Objetos anidados
        assertEquals(gameEntity.getScore().getRating(), game.score().rating());
        assertEquals(gameEntity.getScore().getRatingCount(), game.score().ratingCount());

        // Listas
        assertEquals(gameEntity.getGenres().size(), game.genres().size());
        assertEquals(gameEntity.getGenres().iterator().next().getSlug(), game.genres().get(0).slug());
        assertEquals(gameEntity.getPlatforms().size(), game.platforms().size());
        assertEquals(gameEntity.getPlatforms().iterator().next().getSlug(), game.platforms().get(0).slug());
        assertEquals(gameEntity.getVideos().size(), game.videos().size());
    }

    @Test
    void shouldThrowException_whenGameSlugNotExists() {
        when(gameRepository.findBySlug(GAME_SLUG)).thenReturn(Optional.empty());

        GameNotFoundException exception = assertThrows(
                GameNotFoundException.class,
                () -> gameService.getGame(GAME_SLUG)
        );
        assertTrue(exception.getMessage().contains(GAME_SLUG));
    }

    @Test
    void shouldCall1Time_whenSearchingGames() {
        Pageable pageable = getPageable();
        Page<GameEntity> entityPage = getPageGames(pageable);

        when(gameRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(entityPage);

        PageDTO pageDTO = gameService.getGames(null, null, null, pageable);
        verify(gameRepository, times(1)).findAll(any(Specification.class), eq(pageable));

        assertEquals(entityPage.getSize(), pageDTO.pageSize());
        assertEquals(entityPage.getTotalPages(), pageDTO.totalPages());
        assertEquals(entityPage.getTotalElements(), pageDTO.totalElements());

        GameCatalogDTO gameCatalogDTO = (GameCatalogDTO) pageDTO.content().getFirst();
        GameEntity gameEntity = entityPage.getContent().getFirst();

        assertEquals(gameCatalogDTO.id(), gameEntity.getId());
        assertEquals(gameCatalogDTO.slug(), gameEntity.getSlug());
        assertEquals(gameCatalogDTO.platforms().size(), gameEntity.getPlatforms().size());
    }

    void shouldCall1TimeToPlatformsAndGenres_whenSearchingFilters() {
        when(platformRepository.findPlatformsWithCount()).thenReturn(Collections.emptyList());
        when(genreRepository.findGenresWithCount()).thenReturn(Collections.emptyList());

        FilterDTO filterDTO = gameService.getFilters();

        verify(platformRepository, times(1)).findPlatformsWithCount();
        verify(genreRepository, times(1)).findGenresWithCount();

        assertEquals(filterDTO.platforms().size(), 0);
        assertEquals(filterDTO.genres().size(), 1);
        assertNull(filterDTO.statuses());
    }
}
