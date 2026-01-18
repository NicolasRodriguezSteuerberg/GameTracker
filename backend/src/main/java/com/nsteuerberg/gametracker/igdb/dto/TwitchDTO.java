package com.nsteuerberg.gametracker.igdb.dto;

public record TwitchDTO(
    String access_token,
    Long expires_in,
    String token_type
) {
}
