package com.opendata.domain.tourspot.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.opendata.domain.tourspot.dto.CityDataDto;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotFutureCongestion;

import com.opendata.domain.tourspot.entity.enums.CongestionLevel;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FutureCongestionMapper extends TourSpotBaseMapper<CityDataDto.FutureData, TourSpotFutureCongestion> {

    FutureCongestionMapper INSTANCE = Mappers.getMapper(FutureCongestionMapper.class);

    default TourSpotFutureCongestion mapWithExtra(
            CityDataDto.FutureData dto, TourSpot tourSpot, CongestionLevel level) {

        TourSpotFutureCongestion entity = toEntity(dto);
        entity.assignTourSpot(tourSpot);
        entity.assignCongestion(level);
        return entity;
    }

}
