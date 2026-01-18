package com.nsteuerberg.gametracker.games.persistance.specification;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.GenreEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.PlatformEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class GameSpecification {
    public static Specification<GameEntity> hasPlatforms(List<Long> platformIds) {
        return (root, query, cb) -> {
            if (platformIds == null || platformIds.isEmpty()) return cb.conjunction();
            System.out.println(platformIds);
            Join<GameEntity, PlatformEntity> platforms = root.join("platforms");
            return platforms.get("id").in(platformIds);
        };
    }

    public static Specification<GameEntity> hasGenres(List<Long> genreIds) {
        return (root, query, cb) -> {
            if (genreIds == null || genreIds.isEmpty()) return cb.conjunction();
            Join<GameEntity, GenreEntity> genres = root.join("genres");
            return genres.get("id").in(genreIds);
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
