package com.nsteuerberg.gametracker.library.presentation.controller;

import com.nsteuerberg.gametracker.library.presentation.dto.GameProgressRequest;
import com.nsteuerberg.gametracker.library.service.UserLibraryService;
import com.nsteuerberg.gametracker.library.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users/{userIdOrMe}/library")
@RequiredArgsConstructor
public class UserLibraryController {
    private final UserLibraryService userLibraryService;
    private final AuthUtils authUtils;

    @PostMapping("/{gameId}")
    public void addGame(
            @PathVariable String userIdOrMe,
            @PathVariable String gameId,
            @RequestBody GameProgressRequest gameProgressRequest,
            Authentication authentication
    ) {
        Long userId = authUtils.validateAndGetUserId(userIdOrMe, authentication);
    }

    @PatchMapping("/{gameId}")
    public void updateGame(
            @PathVariable String userIdOrMe,
            @PathVariable Long gameId,
            Authentication authentication
    ) {
        Long userId = authUtils.validateAndGetUserId(userIdOrMe, authentication);
    }

    @DeleteMapping("/{gameId}")
    public void deleteGame(
            @PathVariable String userIdOrMe,
            @PathVariable Long gameId,
            Authentication authentication
    ) {
        Long userId = authUtils.validateAndGetUserId(userIdOrMe, authentication);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void getGames(
            @PathVariable String userIdOrMe,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 50, sort="lastUpdated", direction= Sort.Direction.DESC) Pageable pageable,
            Authentication authentication
    ) {
        Long userId = authUtils.resolveUserId(userIdOrMe, authentication);
    }
}
