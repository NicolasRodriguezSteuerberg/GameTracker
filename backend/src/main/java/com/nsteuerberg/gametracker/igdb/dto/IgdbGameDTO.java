package com.nsteuerberg.gametracker.igdb.dto;

import java.util.List;

public record IgdbGameDTO(
    Long id,
    String name,
    String summary,
    List<CommonDTO> platforms,
    List<CommonDTO> genres,
    Long first_release_date,
    Long updated_at,
    CoverDTO cover
){
}
