package com.nsteuerberg.gametracker.games.persistance.repository;

import com.nsteuerberg.gametracker.games.persistance.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    Optional<GenreEntity> findByIgdbId(Long igdbId);
}
