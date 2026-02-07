package com.nsteuerberg.gametracker.shared.dto;

import java.util.List;

public record FilterDTO (
        List<CountPlatformsDTO> platforms,
        List<CountGenresDTO> genres,
        List<CountStatusDTO> statuses
){
}
