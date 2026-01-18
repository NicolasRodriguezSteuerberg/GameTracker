package com.nsteuerberg.gametracker.auth.presentation.controller;

import com.nsteuerberg.gametracker.auth.presentation.dto.JwtDTO;
import com.nsteuerberg.gametracker.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String helloSecured() {
        return "hello";
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public JwtDTO loginWithGoogle(
            @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
            @RequestParam String tokenId
    ) throws GeneralSecurityException, IOException {
        return authService.login(tokenId, userAgent);
    }

    @PostMapping("refresh")
    @ResponseStatus(HttpStatus.OK)
    public JwtDTO refresh(
            @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
            @RequestParam String refreshToken
    ) {
        return authService.refreshSession(refreshToken, userAgent);
    }
}
