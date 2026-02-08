package com.nsteuerberg.gametracker.games.persistance.entity.embed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ScoreData {
    private Double rating;
    @Column(name = "rating_count")
    private Integer ratingCount;
}
