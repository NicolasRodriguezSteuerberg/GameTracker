package com.nsteuerberg.gametracker.library.persistance.repository;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.library.persistance.entity.LibraryEntryEntity;
import com.nsteuerberg.gametracker.library.utils.GameStatus;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class LibrarySpecification {

    public static Specification<LibraryEntryEntity> hasUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) return cb.conjunction();
            return cb.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<LibraryEntryEntity> withStatuses(Set<GameStatus> gameStatuses) {
        return (root, query, cb) -> {
            if (gameStatuses == null || gameStatuses.isEmpty())
                return cb.conjunction();
            return root.get("status").in(gameStatuses);
        };
    }
    public static Specification<LibraryEntryEntity> withGameTitleLike(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title ==null || title.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.join("game").get("name")),
                    "%" + title.toLowerCase() + "%"
            );
        };
    }

    public static Specification<LibraryEntryEntity> withGenres(Set<String> genresIds) {
        return (root, query, cb) -> {
            if (genresIds == null || genresIds.isEmpty()) {
                return cb.conjunction();
            }
            query.distinct(true);
            return root.join("game")
                    .join("genres")
                    .get("slug")
                    .in(genresIds);
        };
    }

    public static Specification<LibraryEntryEntity> withPlatforms(Set<String> platformsId) {
        return (root, query, criteriaBuilder) -> {
            if (platformsId == null || platformsId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            query.distinct(true);
            return root.join("game")
                    .join("platforms")
                    .get("slug")
                    .in(platformsId);
        };
    }

    public static Specification<LibraryEntryEntity> fetchGameDetails() {
        return (root, query, criteriaBuilder) -> {
            if (query.getResultType() != Long.class) {
                Fetch<LibraryEntryEntity, GameEntity> gameFetch = root.fetch("game", JoinType.LEFT);
                //gameFetch.fetch("genres", JoinType.LEFT);
                gameFetch.fetch("platforms", JoinType.LEFT);
            }
            return criteriaBuilder.conjunction();
        };
    }
}
