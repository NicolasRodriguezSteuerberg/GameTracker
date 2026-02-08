package com.nsteuerberg.gametracker.shared.dto;

public record CountPlatformsDTO (
        Long id,
        String name,
        Long count
){
    public CountPlatformsDTO(Long id, String name) {
        this(id, name, null); // Delega al constructor principal (canónico)
    }
}
