package com.opendata.domain.tourspot.mapper;

import com.opendata.domain.tourspot.dto.CityDataDto;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotEvent;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TourSpotEventMapper {


    @Mapping(source = "thumbnail", target = "tourspotThumbnail")
    @Mapping(source = "url", target = "tourspotUrl")
    TourSpotEvent toTourSpotEvent(CityDataDto.EventData eventData, @Context TourSpot tourSpot);

    @AfterMapping
    default void afterToEntity(@MappingTarget TourSpotEvent tourSpotEvent, @Context TourSpot tourSpot){
        tourSpotEvent.assignTourSpot(tourSpot);
        tourSpot.addEvent(tourSpotEvent);
    }
}
