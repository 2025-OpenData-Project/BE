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


    public static AreaComponentDto from(FilteredArea area, String time){
        return AreaComponentDto.builder()
                .name(area.name())
                .category(area.category())
                .image(area.image())
                .lat(area.lat())
                .lon(area.lon())
                .indoor(area.indoor())
                .congestion_level(area.congestionLevel())
                .time(time)
                .build();
    }


}
