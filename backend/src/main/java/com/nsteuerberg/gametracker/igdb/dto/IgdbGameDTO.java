package com.nsteuerberg.gametracker.igdb.dto;

import java.util.List;

public record IgdbGameDTO(
    Long id,
    String name,
    String slug,
    String summary,
    String storyline,
    Double total_rating,
    Integer total_rating_count,
    List<CommonDTO> platforms,
    List<CommonDTO> genres,
    Long first_release_date,
    Long updated_at,
    CoverDTO cover,
    List<CoverDTO> screenshots,
    List<VideoDTO> videos
){
}
