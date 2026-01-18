package com.nsteuerberg.gametracker.games.service;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.games.persistance.repository.GameRepository;
import com.nsteuerberg.gametracker.games.persistance.specification.GameSpecification;
import com.nsteuerberg.gametracker.games.presentation.dto.response.PageDTO;
import com.nsteuerberg.gametracker.games.presentation.mapper.GameMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public PageDTO getGames(List<Long> platformIds, List<Long> genreIds, String title, Pageable pageable) {
        Specification<GameEntity> spec = Specification
                .where(GameSpecification.hasPlatforms(platformIds))
                .and(GameSpecification.hasTitle(title));

        Page<GameEntity> games = gameRepository.findAll(spec, pageable);
        return GameMapper.toPageCatalogDTO(games);
    }
}
