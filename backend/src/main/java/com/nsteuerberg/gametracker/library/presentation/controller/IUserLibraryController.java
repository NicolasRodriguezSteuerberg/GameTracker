package com.nsteuerberg.gametracker.library.presentation.controller;

import com.nsteuerberg.gametracker.library.presentation.dto.request.GameAddRequest;
import com.nsteuerberg.gametracker.library.presentation.dto.request.GameUpdateRequest;
import com.nsteuerberg.gametracker.library.presentation.dto.response.LibraryGameDTO;
import com.nsteuerberg.gametracker.library.utils.GameStatus;
import com.nsteuerberg.gametracker.shared.dto.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Tag(name = "User Library", description = "Manage games within user libraries")
public interface IUserLibraryController {
    @Operation(
            summary = "Add a game to user's library",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Game added successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized: Can't modify others libraries"),
                    @ApiResponse(responseCode = "404", description = "Game not found"),
                    @ApiResponse(responseCode = "409", description = "Game already exists in the library")
            }
    )
    LibraryGameDTO addGame(
            @Parameter(description = "User ID or 'me' for the authenticated user")
            @PathVariable String userIdOrMe,
            @PathVariable Long gameId,
            @RequestBody @Valid GameAddRequest gameAddRequest,
            @Parameter(hidden = true) Authentication authentication
    );

    @Operation(
            summary = "Update game data in a library",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized: Can't update others users libraries"),
                    @ApiResponse(responseCode = "404", description = "Game or library entry not found")
            }
    )
    LibraryGameDTO updateGame(
            @Parameter(description = "User ID or 'me' for the authenticated user")
            @PathVariable String userIdOrMe,
            @PathVariable Long gameId,
            @Parameter(hidden = true) Authentication authentication,
            @RequestBody @Valid GameUpdateRequest updateRequest
    );

    @Operation(
            summary = "Remove a game from a library",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Game deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized: Can't update others libraries"),
                    @ApiResponse(responseCode = "404", description = "Game or library entry not found in library")
            }
    )
    void deleteGame(
            @Parameter(description = "User ID or 'me' for the authenticated user")
            @PathVariable String userIdOrMe,
            @PathVariable Long gameId,
            @Parameter(hidden = true) Authentication authentication
    );

    @Operation(
            summary = "Get paginated list of games in a user's library",
            description = "Retrieve games from a specific library user with optional filters for status, platforms, genres and titles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved page")
            }
    )
    PageDTO<LibraryGameDTO> getGames(
            @Parameter(description = "User ID or 'me' for the authenticated user")
            @PathVariable String userIdOrMe,
            @RequestParam(required = false) Set<GameStatus> statuses,
            @RequestParam(required = false) Set<String> platformSlugs,
            @RequestParam(required = false) Set<String> genreSlugs,
            @RequestParam(required = false) String title,
            @PageableDefault(size = 50, sort = "lastUpdated", direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(hidden = true) Authentication authentication
    );
}
