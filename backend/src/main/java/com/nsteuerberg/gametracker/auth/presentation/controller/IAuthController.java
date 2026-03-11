package com.nsteuerberg.gametracker.auth.presentation.controller;

import com.nsteuerberg.gametracker.auth.presentation.dto.JwtDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Authentication", description = "Endpoints for authentication")
public interface IAuthController {
    @Operation(
            summary = "Log in with google token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully log in"),
                    @ApiResponse(
                            responseCode = "401", description = "Invalid Token provided",
                            content = @Content
                    )
            }
    )
    JwtDTO loginWithGoogle(
        @RequestHeader("X-Device-Id") String deviceId,
        @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
        @RequestParam String tokenId
    );

    @Operation(
            summary = "Refresh access token",
            description = "Refresh access token by the refresh token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully refreshed"),
                    @ApiResponse(responseCode = "401", description = "Invalid refresh token (inexistent or expired)")
            }
    )
    JwtDTO refresh(
        @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
        @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
        @RequestParam String refreshToken
    );

    @Operation(
            summary = "Log out",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully log out")
            }
    )
    void logout(
        @RequestHeader("X-Device-Id") String deviceId,
        Authentication authentication
    );

}
