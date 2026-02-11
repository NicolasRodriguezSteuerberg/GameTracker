package com.nsteuerberg.gametracker.games.persistance.specification;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.GenreEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.PlatformEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class GameSpecification {
    public static Specification<GameEntity> hasPlatforms(Set<String> platformSlugs) {
        return (root, query, cb) -> {
            if (platformSlugs == null || platformSlugs.isEmpty()) return cb.conjunction();
            Join<GameEntity, PlatformEntity> platforms = root.join("platforms");
            return platforms.get("slug").in(platformSlugs);
        };
    }

    public static Specification<GameEntity> hasGenres(Set<String> genreSlugs) {
        return (root, query, cb) -> {
            if (genreSlugs == null || genreSlugs.isEmpty()) return cb.conjunction();
            Join<GameEntity, GenreEntity> genres = root.join("genres");
            return genres.get("slug").in(genreSlugs);
        };
    }

    public static Specification<GameEntity> hasTitle(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isEmpty()) return cb.conjunction();
            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + title.toLowerCase() + "%"
            );
        };
    }
}
