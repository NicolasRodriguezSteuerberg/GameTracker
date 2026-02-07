package com.nsteuerberg.gametracker.games.presentation.controller;

import com.nsteuerberg.gametracker.shared.dto.PageDTO;
import com.nsteuerberg.gametracker.games.service.GameService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController()
@RequestMapping("games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageDTO getGames(
            @RequestParam(required = false) Set<Long> platforms,
            @RequestParam(required = false) Set<Long> genres,
            @RequestParam(required = false) String title,
            Pageable pageable
    ) {
        return gameService.getGames(platforms, genres, title, pageable);
    }
}
