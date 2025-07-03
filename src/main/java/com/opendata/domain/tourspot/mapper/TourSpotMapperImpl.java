//package com.opendata.domain.tourspot.mapper;
//
//import com.opendata.domain.tourspot.dto.CityDataDto;
//import com.opendata.domain.tourspot.entity.TourSpotFutureCongestion;
//import com.opendata.global.commoncode.service.CommonCodeService;
//import lombok.RequiredArgsConstructor;
//
//import javax.annotation.processing.Generated;
//
//@Generated(
//        value = "org.mapstruct.ap.MappingProcessor",
//        date = "2025-07-01T20:52:00+0900",
//        comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Amazon.com Inc.)"
//)
//@RequiredArgsConstructor
//public class TourSpotMapperImpl implements TourSpotMapper{
//
//    private final CommonCodeService commonCodeService;
//
//    @Override
//    public TourSpotFutureCongestion toDailyCongestion(CityDataDto.LivePopulationStatus futureData) {
//        return TourSpotFutureCongestion.builder()
//                .tourspot(tourSpot)
//                .congestionLvl(commonCodeService.getByCodeNm(futureData.getAreaCongestLvl()))
//                .build();
//    }
//}
