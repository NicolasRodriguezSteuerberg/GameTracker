package com.nsteuerberg.gametracker.games.service;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.games.persistance.repository.GameRepository;
import com.nsteuerberg.gametracker.games.persistance.repository.GenreRepository;
import com.nsteuerberg.gametracker.games.persistance.repository.PlatformRepository;
import com.nsteuerberg.gametracker.games.persistance.specification.GameSpecification;
import com.nsteuerberg.gametracker.games.presentation.dto.response.GameDTO;
import com.nsteuerberg.gametracker.games.service.exceptions.GameNotFoundException;
import com.nsteuerberg.gametracker.shared.dto.FilterDTO;
import com.nsteuerberg.gametracker.shared.dto.PageDTO;
import com.nsteuerberg.gametracker.games.presentation.mapper.GameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;

    public PageDTO getGames(Set<String> platformSlugs, Set<String> genreSlugs, String title, Pageable pageable) {
        Specification<GameEntity> spec = Specification
                .where(GameSpecification.hasPlatforms(platformSlugs))
                .and(GameSpecification.hasGenres(genreSlugs))
                .and(GameSpecification.hasTitle(title));

        Page<GameEntity> games = gameRepository.findAll(spec, pageable);
        return GameMapper.toPageCatalogDTO(games);
    }

    public GameDTO getGame(String slug) {
        GameEntity game = gameRepository.findBySlug(slug).orElseThrow(() -> new GameNotFoundException(slug));
        return GameDTO.fromEntity(game);
    }

    public FilterDTO getFilters() {
        return new FilterDTO(
                platformRepository.findPlatformsWithCount(),
                genreRepository.findGenresWithCount(),
                null
        );
    }
}
