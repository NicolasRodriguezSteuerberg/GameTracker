package com.nsteuerberg.gametracker.games.persistance.repository;

import com.nsteuerberg.gametracker.games.persistance.entity.PlatformEntity;
import com.nsteuerberg.gametracker.shared.dto.CountCommonDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatformRepository extends JpaRepository<PlatformEntity, Long> {
    Optional<PlatformEntity> findByIgdbId(Long igdbId);
    @Query("""
    SELECT new com.nsteuerberg.gametracker.shared.dto.CountCommonDTO(p.slug, p.name)
    FROM PlatformEntity p
    ORDER BY p.name ASC
    """)
    List<CountCommonDTO> findPlatformsWithCount();
}
