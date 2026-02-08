package com.nsteuerberg.gametracker.games.persistance.entity.embed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class GameTimeData {
    @Column(name = "time_main")
    private Integer main;
    @Column(name = "time_main_extras")
    private Integer mainExtras;
    @Column(name = "time_completionist")
    private Integer completionist;
}
