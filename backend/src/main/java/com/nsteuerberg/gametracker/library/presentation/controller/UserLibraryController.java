package com.nsteuerberg.gametracker.library.presentation.controller;

import com.nsteuerberg.gametracker.library.presentation.dto.request.GameAddRequest;
import com.nsteuerberg.gametracker.library.presentation.dto.request.GameUpdateRequest;
import com.nsteuerberg.gametracker.library.presentation.dto.response.LibraryGameDTO;
import com.nsteuerberg.gametracker.library.service.UserLibraryService;
import com.nsteuerberg.gametracker.library.utils.AuthUtils;
import com.nsteuerberg.gametracker.library.utils.GameStatus;
import com.nsteuerberg.gametracker.shared.dto.FilterDTO;
import com.nsteuerberg.gametracker.shared.dto.PageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("users/{userIdOrMe}/library")
@RequiredArgsConstructor
public class UserLibraryController {
    private final UserLibraryService userLibraryService;
    private final AuthUtils authUtils;

    @PostMapping("/{gameId}")
    @ResponseStatus(HttpStatus.CREATED)
    public LibraryGameDTO addGame(
            @PathVariable String userIdOrMe,
            @PathVariable Long gameId,
            @RequestBody @Valid GameAddRequest gameAddRequest,
            Authentication authentication
    ) {
        Long userId = authUtils.validateAndGetUserId(userIdOrMe, authentication);
        return userLibraryService.addGame(userId, gameId, gameAddRequest);
    }

    @PatchMapping("/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public LibraryGameDTO updateGame(
            @PathVariable String userIdOrMe,
            @PathVariable Long gameId,
            Authentication authentication,
            @RequestBody @Valid GameUpdateRequest updateRequest
    ) {
        Long userId = authUtils.validateAndGetUserId(userIdOrMe, authentication);
        return userLibraryService.updateGame(userId, gameId, updateRequest);
    }

    @DeleteMapping("/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(
            @PathVariable String userIdOrMe,
            @PathVariable Long gameId,
            Authentication authentication
    ) {
        Long userId = authUtils.validateAndGetUserId(userIdOrMe, authentication);
        userLibraryService.deleteGame(userId, gameId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageDTO getGames(
            @PathVariable String userIdOrMe,
            @RequestParam(required = false) Set<GameStatus> statuses,
            @RequestParam(required = false) Set<Long> platforms,
            @RequestParam(required = false) Set<Long> genres,
            @RequestParam(required = false) String title,
            @PageableDefault(size = 50, sort="lastUpdated", direction= Sort.Direction.DESC) Pageable pageable,
            Authentication authentication
    ) {
        Long userId = authUtils.resolveUserId(userIdOrMe, authentication);
        return userLibraryService.getLibrary(userId, statuses, platforms, genres, title, pageable);
    }

    @GetMapping("filters")
    @ResponseStatus(HttpStatus.OK)
    public FilterDTO getFilters(
            @PathVariable String userIdOrMe,
            Authentication authentication
    ) {
        Long userId = authUtils.resolveUserId(userIdOrMe, authentication);
        return userLibraryService.getFilters(userId);
    }
}
