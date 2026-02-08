package com.nsteuerberg.gametracker.shared.dto;

public record CountCommonDTO(
        String slug,
        String name,
        Long count
){
    public CountCommonDTO(String slug, String name) {
        this(slug, name, null); // Delega al constructor principal (canónico)
    }
}
