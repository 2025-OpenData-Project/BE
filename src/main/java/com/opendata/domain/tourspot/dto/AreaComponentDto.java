package com.opendata.domain.tourspot.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AreaComponentDto(
        String name,
        String category,
        String image,
        double lat,
        double lon,
        List<CityDataDto.EventData> events,
        boolean indoor,
        int congestion_level,
        String time
) {

}
