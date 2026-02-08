package com.nsteuerberg.gametracker.games.persistance.entity.embed;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoData {
    // id de YOUTUBE
    private String id;
    private String name;
}
