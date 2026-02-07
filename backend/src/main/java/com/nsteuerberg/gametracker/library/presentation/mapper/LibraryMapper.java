package com.nsteuerberg.gametracker.library.presentation.mapper;

import com.nsteuerberg.gametracker.library.persistance.entity.LibraryEntryEntity;
import com.nsteuerberg.gametracker.library.presentation.dto.response.LibraryGameDTO;
import com.nsteuerberg.gametracker.shared.dto.PageDTO;
import org.springframework.data.domain.Page;

public class LibraryMapper {
    public static PageDTO toPageLibraryDTO(Page<LibraryEntryEntity> page) {
        return PageDTO.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .content(page.getContent().stream()
                        .map(LibraryGameDTO::fromEntity)
                        .toList()
                )
                .build();
    }
}
