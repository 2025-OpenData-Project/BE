package com.opendata.domain.apidata.dto;

import com.opendata.domain.apidata.entity.Area;
import lombok.Builder;

import java.util.List;

@Builder
public record AreaComponentDto(
        String name,
        String category,
        String image,
        double lat,
        double lon,
        boolean indoor,
        int congestion_level
) {
    public static AreaComponentDto from(Area area){
        return AreaComponentDto.builder()
                .name(area.getName())
                .category(area.getCategory())
                .image(area.getImage())
                .lat(area.getLatitude())
                .lon(area.getLongitude())
                .indoor(area.isIndoor())
                .congestion_level(area.getCongestion_level())
                .build();
    }
}
