package com.opendata.domain.tourspot.mapper;


import com.opendata.domain.tourspot.dto.MonthlyCongestionDto;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotMonthlyCongestion;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = CongestionLevel.class
)
public interface MonthlyCongestionMapper {
    FutureCongestionMapper INSTANCE = Mappers.getMapper(FutureCongestionMapper.class);


    @Mapping(target = "date", source = "baseYmd")
    @Mapping(target = "congestionLvl", expression = "java(CongestionLevel.fromRate(dto.getCnctrRate()))")
    TourSpotMonthlyCongestion toMonthlyCongestion(
            MonthlyCongestionDto.AddressItem dto,
            @Context TourSpot tourSpot
    );

    @AfterMapping
    default void assignTourSpot(@MappingTarget TourSpotMonthlyCongestion entity,
                                @Context TourSpot tourSpot) {

    }
}
