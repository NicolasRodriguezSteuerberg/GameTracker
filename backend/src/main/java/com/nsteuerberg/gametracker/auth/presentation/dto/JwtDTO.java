package com.nsteuerberg.gametracker.auth.presentation.dto;

public record JwtDTO (
    String accessToken,
    String refreshToken
){
}
