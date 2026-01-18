package com.nsteuerberg.gametracker.igdb.client;

import com.nsteuerberg.gametracker.igdb.dto.CountDTO;
import com.nsteuerberg.gametracker.igdb.dto.IgdbGameDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/v4/games")
public interface IGDBclient {
    @PostExchange
    List<IgdbGameDTO> getGames(
            @RequestHeader("Client-ID") String clientId,
            @RequestHeader("Authorization") String token,
            @RequestBody String text);

    @PostExchange("/count")
    CountDTO getCount(
        @RequestHeader("Client-ID") String clientId,
        @RequestHeader("Authorization") String token,
        @RequestBody String text
    );
}
