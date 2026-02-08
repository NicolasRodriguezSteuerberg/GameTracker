package com.nsteuerberg.gametracker.games.presentation.dto.response;

import com.nsteuerberg.gametracker.games.persistance.entity.GenreEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.PlatformEntity;

import java.util.List;
import java.util.Set;

public record CommonDTO (
    String slug,
    String name
){
    public static List<CommonDTO> fromGenrseEntity(Set<GenreEntity> genres) {
        return genres.stream()
                .map(genre -> new CommonDTO(genre.getSlug(), genre.getName()))
                .toList();
    }

    public static List<CommonDTO> fromPlatformsEntity(Set<PlatformEntity> platforms) {
        return platforms.stream()
                .map(platform -> new CommonDTO(platform.getSlug(), platform.getName()))
                .toList();
    }

}
