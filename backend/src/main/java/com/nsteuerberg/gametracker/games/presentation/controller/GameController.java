package com.nsteuerberg.gametracker.games.presentation.controller;

import com.nsteuerberg.gametracker.games.presentation.dto.response.GameCatalogDTO;
import com.nsteuerberg.gametracker.games.presentation.dto.response.GameDTO;
import com.nsteuerberg.gametracker.shared.dto.FilterDTO;
import com.nsteuerberg.gametracker.shared.dto.PageDTO;
import com.nsteuerberg.gametracker.games.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController()
@RequestMapping("games")
@RequiredArgsConstructor
public class GameController implements GameControllerInterface{
    private final GameService gameService;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageDTO<GameCatalogDTO> getGames(
            @RequestParam(required = false) Set<String> platforms,
            @RequestParam(required = false) Set<String> genres,
            @RequestParam(required = false) String title,
            Pageable pageable
    ) {
        return gameService.getGames(platforms, genres, title, pageable);
    }

    @Override
    @GetMapping("/{slug}")
    @ResponseStatus(HttpStatus.OK)
    public GameDTO getGame(@PathVariable String slug) {
        return gameService.getGame(slug);
    }

    @Override
    @GetMapping("filters")
    @ResponseStatus(HttpStatus.OK)
    public FilterDTO getFilters() {
        return gameService.getFilters();
    }
}
