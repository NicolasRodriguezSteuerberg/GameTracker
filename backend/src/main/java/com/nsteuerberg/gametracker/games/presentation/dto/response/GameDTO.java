package com.nsteuerberg.gametracker.games.presentation.dto.response;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;

import java.time.Instant;
import java.util.List;

public record GameDTO (
        Long id,
        String title,
        String summary,
        String storyline,
        ScoreDTO score,
        String coverUrl,
        List<VideoDTO> videos,
        List<String> screenshots,
        Instant releaseDate,
        List<CommonDTO> platforms,
        List<CommonDTO> genres
){
    public static GameDTO fromEntity(GameEntity game) {
        return new GameDTO(
                game.getId(),
                game.getName(),
                game.getSummary(),
                game.getStoryline(),
                ScoreDTO.fromEntity(game.getScore()),
                game.getCoverUrl(),
                VideoDTO.fromEntity(game.getVideos()),
                game.getScreenshots(),
                game.getFirstReleaseDate(),
                CommonDTO.fromPlatformsEntity(game.getPlatforms()),
                CommonDTO.fromGenrseEntity(game.getGenres())
        );
    }
}
