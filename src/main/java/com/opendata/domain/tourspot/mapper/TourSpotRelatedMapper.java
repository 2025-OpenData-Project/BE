package com.opendata.domain.tourspot.mapper;

import com.opendata.domain.tourspot.dto.MonthlyCongestionDto;
import com.opendata.domain.tourspot.dto.TourSpotRelatedDto;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotMonthlyCongestion;
import com.opendata.domain.tourspot.entity.TourSpotRelated;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TourSpotRelatedMapper {

    TourSpotRelated toEntity(
            String tourSpotCode,
            String tourSpotName,
            String largeCategory,
            String middleCategory,
            String mapX,
            String mapY);

}
