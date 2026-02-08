package com.nsteuerberg.gametracker.shared.dto;

public record CountGenresDTO (
        Long id,
        String name,
        Long count
){
    public CountGenresDTO(Long id, String name) {
        this(id, name, null); // Delega al constructor principal (canónico)
    }
}
