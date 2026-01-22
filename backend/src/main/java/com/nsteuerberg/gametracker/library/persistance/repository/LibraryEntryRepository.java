package com.nsteuerberg.gametracker.library.persistance.repository;

import com.nsteuerberg.gametracker.library.persistance.entity.GameUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryEntryRepository extends JpaRepository<LibraryEntryRepository, GameUserId> {
}
