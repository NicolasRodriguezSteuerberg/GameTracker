package com.nsteuerberg.gametracker.auth.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GoogleConfig {
    private final String clientId;

    public GoogleConfig(SecurityProperties securityProperties) {
        this.clientId = securityProperties.google().clientId();
        System.out.println("Client id: " + clientId);
    }

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),new GsonFactory())
                .setAudience(List.of(clientId))
                .build();
    }
}
