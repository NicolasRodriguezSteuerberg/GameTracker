package com.nsteuerberg.gametracker.shared.dto;

import java.util.List;

public record FilterDTO (
        List<CountCommonDTO> platforms,
        List<CountCommonDTO> genres,
        List<CountStatusDTO> statuses
){
}
