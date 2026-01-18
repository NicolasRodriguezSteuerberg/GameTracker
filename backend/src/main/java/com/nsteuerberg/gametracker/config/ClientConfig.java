package com.nsteuerberg.gametracker.config;

import com.nsteuerberg.gametracker.igdb.client.IGDBclient;
import com.nsteuerberg.gametracker.igdb.client.TwitchClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {
    @Bean
    IGDBclient igdBclient() {
        RestClient restClient = RestClient.builder().baseUrl("https://api.igdb.com").build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory.builderFor(adapter).build().createClient(IGDBclient.class);
    }

    @Bean
    TwitchClient twitchClient() {
        RestClient restClient = RestClient.builder().baseUrl("https://id.twitch.tv").build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory.builderFor(adapter).build().createClient(TwitchClient.class);
    }
}
