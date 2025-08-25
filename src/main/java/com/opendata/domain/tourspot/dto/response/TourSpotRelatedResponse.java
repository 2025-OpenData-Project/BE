package com.opendata.domain.tourspot.dto.response;

public record TourSpotRelatedResponse(
        Long id,
        String tourSpotCode,
        String tourSpotName,
        String largeCtgr,
        String middleCtgr,
        double mapX,
        double mapY
) {
}
