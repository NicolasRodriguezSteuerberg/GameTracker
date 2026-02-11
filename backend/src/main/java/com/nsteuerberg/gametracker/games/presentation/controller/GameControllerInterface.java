package com.nsteuerberg.gametracker.games.presentation.controller;

import com.nsteuerberg.gametracker.games.presentation.dto.response.GameDTO;
import com.nsteuerberg.gametracker.shared.dto.FilterDTO;
import com.nsteuerberg.gametracker.shared.dto.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Tag(name = "Games", description = "Endpoints for retrieving game information and available filters")
public interface GameControllerInterface {

    @Operation(
            summary = "Get a paginated list of games",
            description = "Retrieve games with optional filters for platforms, genres, and titles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
            }
    )
    PageDTO getGames(
            @Parameter(description = "Set of platform names to filter by (e.g., win, ps5)")
            @RequestParam(required = false) Set<String> platforms,
            @Parameter(description = "Set of genre names to filter by (e.g., role-playing-rpg, sport)")
            @RequestParam(required = false) Set<String> genres,
            @Parameter(description = "Partial or full title of the game")
            @RequestParam(required = false) String title,
            @Parameter(description = "Pagination parameters (page, size, sort)")
            Pageable pageable
    );

    @Operation(
            summary = "Get game details by slug",
            description = "Returns a single game identified by its unique URL-friendly slug.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Game found"),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            }
    )
    GameDTO getGame(@PathVariable String slug);

    @Operation(
            summary = "Get available filters",
            description = "Returns the list of all available platforms and genres to use in search filters."
    )
    FilterDTO getFilters();
}
