package com.nsteuerberg.gametracker.games.presentation.dto.response;

import com.nsteuerberg.gametracker.games.persistance.entity.embed.ScoreData;

public record ScoreDTO (
        Double rating,
        Integer ratingCount
){
    public static ScoreDTO fromEntity(ScoreData score) {
        if (score == null) return null;
        return new ScoreDTO(score.getRating(), score.getRatingCount());
    }
}
