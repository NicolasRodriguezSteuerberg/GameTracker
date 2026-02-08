package com.nsteuerberg.gametracker.library.persistance.repository;

import com.nsteuerberg.gametracker.library.persistance.entity.GameUserId;
import com.nsteuerberg.gametracker.library.persistance.entity.LibraryEntryEntity;
import com.nsteuerberg.gametracker.shared.dto.CountCommonDTO;
import com.nsteuerberg.gametracker.shared.dto.CountStatusDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryEntryRepository extends JpaRepository<LibraryEntryEntity, GameUserId>, JpaSpecificationExecutor<LibraryEntryEntity> {
    @Query("""
    SELECT new com.nsteuerberg.gametracker.shared.dto.CountCommonDTO(ge.slug, ge.name, COUNT(le))
        FROM LibraryEntryEntity le
        JOIN le.game ga
        JOIN ga.genres ge
        WHERE le.user.id = :userId
        GROUP BY ge.id
        ORDER BY ge.name ASC
     """)
    List<CountCommonDTO> findGenresWithCount(Long userId);

    @Query("""
    SELECT new com.nsteuerberg.gametracker.shared.dto.CountCommonDTO(pl.slug, pl.name, COUNT(le))
        FROM LibraryEntryEntity le
        JOIN le.game ga
        JOIN ga.platforms pl
        WHERE le.user.id = :userId
        GROUP BY pl.id
        ORDER BY pl.name ASC
     """)
    List<CountCommonDTO> findPlatformsWithCount(Long userId);

    @Query("""
    SELECT new com.nsteuerberg.gametracker.shared.dto.CountStatusDTO(le.status, COUNT(le))
        FROM LibraryEntryEntity le
        WHERE le.user.id = :userId
        GROUP BY le.status
        ORDER BY le.status ASC
    """)
    List<CountStatusDTO> findStatusWithCount(Long userId);

}
