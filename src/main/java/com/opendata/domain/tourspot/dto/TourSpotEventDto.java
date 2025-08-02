package com.opendata.domain.tourspot.dto;

public record TourSpotEventDto(
        Long tourspotEventId,
        String eventName,
        String eventPeriod,
        String eventPlace,
        Double eventX,
        Double eventY,
        String tourspotThumbnail,
        String tourspotUrl
) {}