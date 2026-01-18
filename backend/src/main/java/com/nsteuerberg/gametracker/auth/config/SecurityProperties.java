package com.nsteuerberg.gametracker.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.util.List;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
    List<String> adminEmails,
    GoogleConfig google,
    Token token
) {
    public record GoogleConfig(
            String clientId,
            String clientSecret
    ) {}
    public record Token(
        Refresh refresh,
        Jwt jwt
    ) {
        public record Refresh(
                Integer expiresInDays
        ) {}

        public record Jwt(
                String witUser,
                Integer expiresInMinutes,
                Rsa rsa
        ) {
            public record Rsa(
                    Resource privateKey,
                    Resource publicKey
            ) {}
        }
    }
}
