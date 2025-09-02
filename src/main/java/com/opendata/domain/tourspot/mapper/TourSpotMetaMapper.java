package com.opendata.domain.tourspot.mapper;

import com.opendata.domain.tourspot.dto.response.TourSpotMetaResponse;
import com.opendata.domain.tourspot.entity.TourSpot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import javax.swing.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TourSpotMetaMapper {

    @Mapping(target = "tourspotId", source = "tourspot.tourspotId")
    @Mapping(target = "tourspotNm", source = "tourspot.tourspotNm")
    TourSpotMetaResponse toResponse(TourSpot tourspot, String imageUrl, String congestionLabel);
}
