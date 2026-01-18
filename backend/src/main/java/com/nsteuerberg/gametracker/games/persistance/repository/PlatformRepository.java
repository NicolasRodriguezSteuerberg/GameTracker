package com.nsteuerberg.gametracker.games.persistance.repository;

import com.nsteuerberg.gametracker.games.persistance.entity.PlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformRepository extends JpaRepository<PlatformEntity, Long> {
    Optional<PlatformEntity> findByIgdbId(Long igdbId);
}
