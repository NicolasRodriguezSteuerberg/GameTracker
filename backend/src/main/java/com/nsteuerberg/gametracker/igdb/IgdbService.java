package com.nsteuerberg.gametracker.igdb;


import com.nsteuerberg.gametracker.igdb.client.IGDBclient;
import com.nsteuerberg.gametracker.igdb.client.TwitchClient;
import com.nsteuerberg.gametracker.igdb.dto.IgdbGameDTO;
import com.nsteuerberg.gametracker.igdb.dto.TwitchDTO;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IgdbService {
    private final String clientId;
    private final String clientSecret;
    private final TwitchClient twitchClient;
    private final IGDBclient igdBclient;
    private final TaskScheduler taskScheduler;
    private volatile String authorization;
    public static final Set<Long> VALID_PLATFORM_IDS = Set.of(
            3L, // Linux
            6L, // PC
            14L, // MAC
            20L, // DS
            37L, // 3DS
            48L, // Play 4
            130L, // switch
            167L, // Play 5
            508L // switch 2
    );
    private final String PLATFORM_FILTER_STRING = VALID_PLATFORM_IDS.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(",", "(", ")"));

    private Logger logger = LoggerFactory.getLogger(IgdbService.class);

    public IgdbService(
            @Value("${app.igdb.client-id}") String clientId,
            @Value("${app.igdb.client-secret}") String clientSecret,
            TwitchClient twitchClient, IGDBclient igdBclient, TaskScheduler taskScheduler
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.twitchClient = twitchClient;
        this.igdBclient = igdBclient;
        this.taskScheduler = taskScheduler;
    }
    @PostConstruct
    private void init() {
        getAuthorization();
    }

    private void getAuthorization() {
        try {
            TwitchDTO twitchDTO = twitchClient.getToken(clientId, clientSecret, "client_credentials");
            authorization = "Bearer " + twitchDTO.access_token();

            long refreshSeconds = twitchDTO.expires_in() - 600;
            if (refreshSeconds < 60) refreshSeconds = 60;

            Instant nextExecution = Instant.now().plusSeconds(refreshSeconds);
            taskScheduler.schedule(this::getAuthorization, nextExecution);
        } catch (Exception e) {
            logger.error("IGDB Service:: error recogiendo el token de twitch -> {}: {}", e.getClass().getName(), e.getMessage());

            // si falla hacemos el reintento
            Instant retryTime = Instant.now().plusSeconds(60);
            taskScheduler.schedule(this::getAuthorization, retryTime);
        }
    }

    public Long getCount(Instant lastSync) {
        String where = getWhere(lastSync);
        System.out.println(where);
        return igdBclient.getCount(clientId, authorization, getWhere(lastSync)).count();
    }

    public List<IgdbGameDTO> getGames(Instant lastSync, int limit, Long offset) {
        return igdBclient.getGames(clientId, authorization,
                getFields() + "\n" +
                getWhere(lastSync) + "\n" +
                "sort id asc;\n" +
                "limit " + limit + ";\n" +
                "offset " + offset + ";"
        );
    }

    private String getFields() {
        return "fields id, name, summary, cover.url, platforms.name, genres.name, first_release_date, updated_at;";
    }

    private String getWhere(Instant lastSync) {
        return "where updated_at > " + lastSync.getEpochSecond() +
                " & game_type = 0 & platforms = " + PLATFORM_FILTER_STRING + ";";
    }

}
