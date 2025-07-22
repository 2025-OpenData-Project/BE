package com.opendata.domain.tourspot.mapper;

import com.opendata.domain.tourspot.dto.CityDataDto;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotCurrentCongestion;

import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import org.mapstruct.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = CongestionLevel.class
)

public interface CurrentCongestionMapper {

    @Mapping(target = "congestionLvl", expression = "java(CongestionLevel.resolve(dto.getAreaCongestLvl()))")
    TourSpotCurrentCongestion toEntity(CityDataDto.LivePopulationStatus dto, String fcstTime, @Context TourSpot tourSpot);

    @AfterMapping
    default void afterToEntity(
            @MappingTarget TourSpotCurrentCongestion entity,
            @Context TourSpot tourSpot
    ) {
        entity.assignTourSpot(tourSpot);
        tourSpot.addCurrentCongestion(entity);
    }
}
