package com.nsteuerberg.gametracker.games.presentation.dto.response;

import com.nsteuerberg.gametracker.games.persistance.entity.embed.VideoData;

import java.util.List;

public record VideoDTO (
        String id,
        String name
){
    public static List<VideoDTO> fromEntity(List<VideoData> videos) {
        return videos.stream()
                .map(video -> new VideoDTO(video.getId(), video.getName()))
                .toList();
    }
}
