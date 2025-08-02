package com.opendata.domain.tourspot.mapper;

import com.opendata.domain.address.entity.Address;
import com.opendata.domain.tourspot.dto.AddressDto;
import com.opendata.domain.tourspot.dto.CityDataDto;
import com.opendata.domain.tourspot.dto.TourSpotEventDto;
import com.opendata.domain.tourspot.dto.TourSpotTagDto;
import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotCurrentCongestion;
import com.opendata.domain.tourspot.entity.TourSpotEvent;
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
    @Mapping(target = "address", source = "address")
    @Mapping(target = "congestionLabel", source = "congestion")
    @Mapping(target = "tourSpotEvents", source = "events")
    @Mapping(target = "tourSpotTags", source = "tags")
    TourSpotDetailResponse toResponse(
            TourSpot tourSpot,
            AddressDto address,
            String congestion,
            List<TourSpotEventDto> events,
            List<TourSpotTagDto> tags
    );

    List<TourSpotEventDto> toEventDtos(List<TourSpotEvent> events);
    List<TourSpotTagDto> toTagDtos(List<TourSpotTag> tags);
    AddressDto toAddressDto(Address address);

}
