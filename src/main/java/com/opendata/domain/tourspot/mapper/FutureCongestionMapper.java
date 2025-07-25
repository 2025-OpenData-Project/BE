package com.opendata.domain.tourspot.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.opendata.domain.tourspot.dto.CityDataDto;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotFutureCongestion;

import com.opendata.domain.tourspot.entity.enums.CongestionLevel;

import org.mapstruct.*;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FutureCongestionMapper {

    TourSpotFutureCongestion toTourSpotFutureCongestion(CityDataDto.FutureData dto, @Context TourSpot tourSpot, CongestionLevel congestionLvl);

    @AfterMapping
    default void afterToEntity(
            @MappingTarget TourSpotFutureCongestion entity,
            @Context TourSpot tourSpot
    ) {

        entity.assignTourSpot(tourSpot);
        tourSpot.addFutureCongestion(entity);
    }
}
