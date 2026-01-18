package com.nsteuerberg.gametracker.igdb.client;

import com.nsteuerberg.gametracker.igdb.dto.TwitchDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("oauth2/token")
public interface TwitchClient {
    @PostExchange()
    TwitchDTO getToken(
        @RequestParam("client_id") String clientId,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("grant_type") String grantType
    );
}
