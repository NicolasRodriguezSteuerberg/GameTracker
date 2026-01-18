package com.nsteuerberg.gametracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GameTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameTrackerApplication.class, args);
    }

}
