package com.opendata.domain.tourspot.mapper;

import com.opendata.domain.address.entity.Address;
import com.opendata.domain.tourspot.dto.AddressDto;
import com.opendata.domain.tourspot.dto.CityDataDto;
import com.opendata.domain.tourspot.dto.TourSpotEventDto;
import com.opendata.domain.tourspot.dto.TourSpotMonthlyCongestionDto;
import com.opendata.domain.tourspot.dto.TourSpotTagDto;
import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotCurrentCongestion;
import com.opendata.domain.tourspot.entity.TourSpotEvent;
import com.opendata.domain.tourspot.entity.TourSpotMonthlyCongestion;
import com.opendata.domain.tourspot.entity.TourSpotTag;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = CongestionLevel.class)
public interface TourSpotDetailMapper {

    @Mapping(target = "tourspotNm", source = "tourSpot.tourspotNm")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "congestionLabel", source = "congestion")
    @Mapping(target = "tourSpotEvents", source = "events")
    @Mapping(target = "tourSpotTags", source = "tags")
    @Mapping(target = "tourSpotMonthlyCongestionDtos", source = "monthlyCongestions")
    TourSpotDetailResponse toResponse(
            TourSpot tourSpot,
            String imageUrl,
            AddressDto address,
            String congestion,
            List<TourSpotEventDto> events,
            List<TourSpotTagDto> tags,
            List<TourSpotMonthlyCongestionDto> monthlyCongestions
    );

    List<TourSpotEventDto> toEventDtos(List<TourSpotEvent> events);
    List<TourSpotTagDto> toTagDtos(List<TourSpotTag> tags);
    AddressDto toAddressDto(Address address);
    List<TourSpotMonthlyCongestionDto> toMonthlyCongestionDtos(List<TourSpotMonthlyCongestion> monthlyCongestion);

}
