package com.nsteuerberg.gametracker.games.persistance.repository;

import com.nsteuerberg.gametracker.games.persistance.entity.GenreEntity;
import com.nsteuerberg.gametracker.shared.dto.CountGenresDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    Optional<GenreEntity> findByIgdbId(Long igdbId);

    @Query("""
    SELECT new com.nsteuerberg.gametracker.shared.dto.CountGenresDTO(ge.id, ge.name)
    FROM GenreEntity ge
    ORDER BY ge.name ASC
    """)
    List<CountGenresDTO> findGenresWithCount();
}
