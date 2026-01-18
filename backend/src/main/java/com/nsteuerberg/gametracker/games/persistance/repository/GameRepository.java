package com.nsteuerberg.gametracker.games.persistance.repository;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long>, JpaSpecificationExecutor<GameEntity> {
    List<GameEntity> findByIgdbIdIn(List<Long> igdbIds);

    @EntityGraph(attributePaths = {"platforms"})
    Page<GameEntity> findAll(Specification<GameEntity> spec, Pageable pageable);

    @Query("SELECT MAX(game.lastUpdated) FROM GameEntity game")
    Optional<Instant> getLatestUpdate();
}
