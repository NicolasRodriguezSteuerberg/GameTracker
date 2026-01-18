package com.nsteuerberg.gametracker.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.nsteuerberg.gametracker.auth.config.SecurityProperties;
import com.nsteuerberg.gametracker.auth.persistance.entity.RefreshTokenEntity;
import com.nsteuerberg.gametracker.auth.persistance.entity.UserEntity;
import com.nsteuerberg.gametracker.auth.persistance.repository.RefreshRepository;
import com.nsteuerberg.gametracker.auth.persistance.repository.UserRepository;
import com.nsteuerberg.gametracker.auth.presentation.dto.JwtDTO;
import com.nsteuerberg.gametracker.auth.service.exceptions.InvalidTokenException;
import com.nsteuerberg.gametracker.auth.utils.TokenUtils;
import com.nsteuerberg.gametracker.auth.utils.enums.RoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;

    private final Integer refreshExpiresInDays;
    private final List<String> adminUsers;

    private final static Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(JwtService jwtService, GoogleIdTokenVerifier googleIdTokenVerifier, SecurityProperties securityProperties, RefreshRepository refreshRepository, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.googleIdTokenVerifier = googleIdTokenVerifier;
        this.refreshRepository = refreshRepository;
        this.userRepository = userRepository;
        this.refreshExpiresInDays = securityProperties.token().refresh().expiresInDays();
        this.adminUsers = securityProperties.adminEmails();
    }

    public JwtDTO login(String requestIdToken, String deviceId) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = googleIdTokenVerifier.verify(requestIdToken);
        if (idToken == null) throw new InvalidTokenException("token invalido");

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();

        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    String name = (String) payload.get("name");
                    RoleEnum role = RoleEnum.USER;
                    if (adminUsers.contains(email)){
                        role = RoleEnum.ADMIN;
                    }
                    return userRepository.save(UserEntity.builder()
                            .email(email)
                            .name(name)
                            .createdAt(Instant.now())
                            .role(role)
                            .build()
                    );
                });
        return createAndSaveSession(user, deviceId);
    }

    public JwtDTO refreshSession(String requestRefreshToken, String deviceId) {
        RefreshTokenEntity refreshToken = refreshRepository.findByToken(TokenUtils.hashToken(requestRefreshToken))
                .orElseThrow(() -> new InvalidTokenException("Token inválido"));

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshRepository.delete(refreshToken);
            throw new InvalidTokenException("Token expirado");
        }

        return createAndSaveSession(refreshToken.getUser(), deviceId);
    }

    private JwtDTO createAndSaveSession(UserEntity user, String deviceId) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = TokenUtils.generateToken();
        String hashedToken = TokenUtils.hashToken(refreshToken);

        RefreshTokenEntity refreshTokenEntity = refreshRepository.findByUserAndDeviceId(user, deviceId)
                .orElse(RefreshTokenEntity.builder()
                        .user(user)
                        .deviceId(deviceId)
                        .build());

        refreshTokenEntity.setToken(hashedToken);
        refreshTokenEntity.setExpiresAt(Instant.now()
                .plus(refreshExpiresInDays, ChronoUnit.DAYS)
        );
        refreshRepository.save(refreshTokenEntity);

        return new JwtDTO(accessToken, refreshToken);
    }

    @Scheduled(cron = "@daily")
    private void deleteOldData(){
        int deleted = refreshRepository.deleteExpiredTokens(Instant.now());
        logger.info("Deleted {} old refresh tokens", deleted);
    }
}
