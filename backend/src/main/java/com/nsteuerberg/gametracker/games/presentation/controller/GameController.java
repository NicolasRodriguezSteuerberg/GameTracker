package com.nsteuerberg.gametracker.games.presentation.controller;

import com.nsteuerberg.gametracker.games.presentation.dto.response.PageDTO;
import com.nsteuerberg.gametracker.games.service.GameService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam(required = false) List<Long> platformIds,
            @RequestParam(required = false) List<Long> genreIds,
            @RequestParam(required = false) String title,
            Pageable pageable
    ) {
        return gameService.getGames(platformIds, genreIds, title, pageable);
    }
}
