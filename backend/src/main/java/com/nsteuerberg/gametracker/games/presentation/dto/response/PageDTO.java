package com.nsteuerberg.gametracker.games.presentation.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PageDTO (
        List<?> content,
        int pageNumber,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean last
){
}
