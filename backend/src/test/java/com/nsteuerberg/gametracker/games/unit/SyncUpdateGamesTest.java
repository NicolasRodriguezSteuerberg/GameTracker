package com.nsteuerberg.gametracker.games.unit;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.GenreEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.PlatformEntity;
import com.nsteuerberg.gametracker.games.persistance.repository.GameRepository;
import com.nsteuerberg.gametracker.games.persistance.repository.GenreRepository;
import com.nsteuerberg.gametracker.games.persistance.repository.PlatformRepository;
import com.nsteuerberg.gametracker.games.service.usecase.SyncUpdateGamesUseCase;
import com.nsteuerberg.gametracker.igdb.IgdbService;
import com.nsteuerberg.gametracker.igdb.dto.IgdbGameDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.nsteuerberg.gametracker.games.helpers.SyncUpdateGamesTestHelpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SyncUpdateGamesTest {
    @Mock
    GameRepository gameRepository;
    @Mock
    GenreRepository genreRepository;
    @Mock
    PlatformRepository platformRepository;
    @Mock
    IgdbService igdbService;

    @InjectMocks
    SyncUpdateGamesUseCase syncUpdateGamesUseCase;

    @Test
    void shouldSyncGamesSuccessfully() {
        when(gameRepository.getLatestUpdate()).thenReturn(Optional.of(LAST_SYNC));
        when(igdbService.getCount(LAST_SYNC)).thenReturn(1L);
        when(igdbService.getGames(LAST_SYNC, 500, 0L)).thenReturn(List.of(createValidTestGameDto()));
        when(gameRepository.findByIgdbIdIn(anyList())).thenReturn(List.of());
        when(genreRepository.findByIgdbId(any())).thenReturn(Optional.empty());
        when(genreRepository.save(any())).thenReturn(createTestGenre());
        when(platformRepository.findByIgdbId(any())).thenReturn(Optional.empty());
        when(platformRepository.save(any())).thenReturn(createTestPlatform());

        syncUpdateGamesUseCase.execute();

        ArgumentCaptor<List<GameEntity>> captor = ArgumentCaptor.forClass(List.class);

        verify(gameRepository).saveAll(captor.capture());

        List<GameEntity> saved = captor.getValue();
        GameEntity game = saved.get(0);

        assertEquals(1, saved.size());
        assertEquals(GAME_NAME, game.getName());

        assertEquals(1, game.getGenres().size());
        assertEquals("Action", game.getGenres().iterator().next().getName());

        assertEquals(1, game.getPlatforms().size());
        assertEquals("Nintendo DS", game.getPlatforms().iterator().next().getName());

        assertTrue(game.getCoverUrl().startsWith("https:"));
        assertTrue(game.getCoverUrl().contains("t_cover_big"));

        assertEquals(4.0, game.getScore().getRating());
        assertEquals(1000, game.getScore().getRatingCount());
    }

    @Test
    void shouldSkipGameWithNullPlatforms() {
        when(gameRepository.getLatestUpdate()).thenReturn(Optional.of(LAST_SYNC));
        when(igdbService.getCount(LAST_SYNC)).thenReturn(1L);
        when(igdbService.getGames(LAST_SYNC, 500, 0L)).thenReturn(List.of(createInvalidTestGameDtoByPlatform()));
        when(gameRepository.findByIgdbIdIn(anyList())).thenReturn(List.of());

        syncUpdateGamesUseCase.execute();

        ArgumentCaptor<List<GameEntity>> captor = ArgumentCaptor.forClass(List.class);

        verify(gameRepository).saveAll(captor.capture());

        List<GameEntity> saved = captor.getValue();
        assertEquals(0, saved.size());
    }

    @Test
    void shouldSkipGameWithNullGenres() {
        when(gameRepository.getLatestUpdate()).thenReturn(Optional.of(LAST_SYNC));
        when(igdbService.getCount(LAST_SYNC)).thenReturn(1L);
        when(igdbService.getGames(LAST_SYNC, 500, 0L)).thenReturn(List.of(createInvalidTestGameDtoByGenre()));
        when(gameRepository.findByIgdbIdIn(anyList())).thenReturn(List.of());

        syncUpdateGamesUseCase.execute();

        ArgumentCaptor<List<GameEntity>> captor = ArgumentCaptor.forClass(List.class);

        verify(gameRepository).saveAll(captor.capture());

        List<GameEntity> saved = captor.getValue();
        assertEquals(0, saved.size());
    }

    @Test
    void shouldSkipGameWithNullCover() {
        when(gameRepository.getLatestUpdate()).thenReturn(Optional.of(LAST_SYNC));
        when(igdbService.getCount(LAST_SYNC)).thenReturn(1L);
        when(igdbService.getGames(LAST_SYNC, 500, 0L)).thenReturn(List.of(createInvalidTestGameDtoByCover()));
        when(gameRepository.findByIgdbIdIn(anyList())).thenReturn(List.of());

        syncUpdateGamesUseCase.execute();

        ArgumentCaptor<List<GameEntity>> captor = ArgumentCaptor.forClass(List.class);

        verify(gameRepository).saveAll(captor.capture());

        List<GameEntity> saved = captor.getValue();
        assertEquals(0, saved.size());
    }

    @Test
    void shouldSkipGameWithNullReleaseDate() {
        when(gameRepository.getLatestUpdate()).thenReturn(Optional.of(LAST_SYNC));
        when(igdbService.getCount(LAST_SYNC)).thenReturn(1L);
        when(igdbService.getGames(LAST_SYNC, 500, 0L)).thenReturn(List.of(createInvalidTestGameDtoByReleaseDate()));
        when(gameRepository.findByIgdbIdIn(anyList())).thenReturn(List.of());

        syncUpdateGamesUseCase.execute();

        ArgumentCaptor<List<GameEntity>> captor = ArgumentCaptor.forClass(List.class);

        verify(gameRepository).saveAll(captor.capture());

        List<GameEntity> saved = captor.getValue();
        assertEquals(0, saved.size());
    }

    @Test
    void shouldCallIgdbMultipleTimes() {
        when(gameRepository.getLatestUpdate()).thenReturn(Optional.of(LAST_SYNC));
        when(igdbService.getCount(LAST_SYNC)).thenReturn(1500L);
        when(igdbService.getGames(LAST_SYNC, 500, 0L)).thenReturn(List.of(createValidTestGameDto()));
        when(gameRepository.findByIgdbIdIn(anyList())).thenReturn(List.of());
        when(genreRepository.findByIgdbId(any())).thenReturn(Optional.empty());
        when(genreRepository.save(any())).thenReturn(createTestGenre());
        when(platformRepository.findByIgdbId(any())).thenReturn(Optional.empty());
        when(platformRepository.save(any())).thenReturn(createTestPlatform());

        syncUpdateGamesUseCase.execute();

        verify(igdbService, times(3)).getGames(eq(LAST_SYNC), eq(500), any(Long.class));
        verify(gameRepository, times(3)).saveAll(anyList());
    }

    @Test
    void shouldUpdateExistingGame() {
        IgdbGameDTO dto = createValidTestGameDto();
        GameEntity existingGame = new GameEntity();
        existingGame.setIgdbId(dto.id());

        when(gameRepository.getLatestUpdate()).thenReturn(Optional.of(LAST_SYNC));
        when(igdbService.getCount(LAST_SYNC)).thenReturn(1L);
        when(igdbService.getGames(LAST_SYNC, 500, 0L)).thenReturn(List.of(dto));
        when(gameRepository.findByIgdbIdIn(anyList())).thenReturn(List.of(existingGame));
        when(genreRepository.findByIgdbId(any())).thenReturn(Optional.empty());
        when(genreRepository.save(any())).thenReturn(createTestGenre());
        when(platformRepository.findByIgdbId(any())).thenReturn(Optional.empty());
        when(platformRepository.save(any())).thenReturn(createTestPlatform());

        syncUpdateGamesUseCase.execute();

        ArgumentCaptor<List<GameEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(gameRepository).saveAll(captor.capture());

        GameEntity saved = captor.getValue().get(0);
        assertEquals(existingGame, saved);
    }

    @Test
    void shouldReuseGenreAndPlatformFromCache() {
        when(gameRepository.getLatestUpdate()).thenReturn(Optional.of(LAST_SYNC));
        when(igdbService.getCount(LAST_SYNC)).thenReturn(2L);
        when(igdbService.getGames(LAST_SYNC, 500, 0L)).thenReturn(List.of(createValidTestGameDto(), createValidTestGameDto()));
        when(gameRepository.findByIgdbIdIn(anyList())).thenReturn(List.of());
        when(genreRepository.findByIgdbId(any())).thenReturn(Optional.empty());
        when(genreRepository.save(any())).thenReturn(createTestGenre());
        when(platformRepository.findByIgdbId(any())).thenReturn(Optional.empty());
        when(platformRepository.save(any())).thenReturn(createTestPlatform());

        syncUpdateGamesUseCase.execute();

        verify(genreRepository, times(1)).findByIgdbId(anyLong());
        verify(genreRepository, times(1)).save(any(GenreEntity.class));
        verify(platformRepository, times(1)).findByIgdbId(anyLong());
        verify(platformRepository, times(1)).save(any(PlatformEntity.class));
    }
}
