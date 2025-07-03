package com.opendata.domain.tourspot.dto;

import java.time.LocalDateTime;
import java.util.List;

public record FilteredArea(
        String areaId,
        String name,
        String category,
        String description,
        String image,
        boolean indoor,
        double lat,
        double lon,
        int congestionLevel,
        List<CityDataDto.EventData> events,
        LocalDateTime time
) {}