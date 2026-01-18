package com.nsteuerberg.gametracker.auth.persistance.repository;

import com.nsteuerberg.gametracker.auth.persistance.entity.RefreshTokenEntity;
import com.nsteuerberg.gametracker.auth.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshRepository extends JpaRepository<RefreshTokenEntity, Long> {
    @EntityGraph(attributePaths={"user"})
    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByUserAndDeviceId(UserEntity user, String deviceId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.expiresAt < :now")
    int deleteExpiredTokens(Instant now);
}
