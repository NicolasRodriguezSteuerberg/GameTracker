package com.nsteuerberg.gametracker.library.persistance.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.*;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GameUserId implements Serializable {
    private Long userId;
    private Long gameId;
}