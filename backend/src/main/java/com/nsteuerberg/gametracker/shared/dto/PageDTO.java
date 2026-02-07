package com.nsteuerberg.gametracker.shared.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PageDTO (
        List<?> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
){
}
