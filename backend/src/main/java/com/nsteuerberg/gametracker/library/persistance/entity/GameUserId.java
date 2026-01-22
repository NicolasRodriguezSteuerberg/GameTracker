package com.nsteuerberg.gametracker.library.persistance.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record GameUserId (
    Long userId,
    Long gameId
) implements Serializable {
}
