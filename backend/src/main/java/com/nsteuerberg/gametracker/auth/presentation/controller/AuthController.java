package com.nsteuerberg.gametracker.auth.presentation.controller;

import com.nsteuerberg.gametracker.auth.presentation.dto.JwtDTO;
import com.nsteuerberg.gametracker.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public JwtDTO loginWithGoogle(
            @RequestHeader("X-Device-Id") String deviceId,
            @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
            @RequestParam String tokenId
    ) throws GeneralSecurityException, IOException {
        return authService.login(tokenId, deviceId, userAgent);
    }

    @PostMapping("refresh")
    @ResponseStatus(HttpStatus.OK)
    public JwtDTO refresh(
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
            @RequestParam String refreshToken
    ) {
        return authService.refreshSession(refreshToken, deviceId, userAgent);
    }

    @DeleteMapping("logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(
            @RequestHeader("X-Device-Id") String deviceId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        authService.deleteSession(userId, deviceId);
    }
}
