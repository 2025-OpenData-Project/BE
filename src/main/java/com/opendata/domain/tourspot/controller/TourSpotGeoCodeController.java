//package com.opendata.domain.tourspot.controller;
//
//import com.opendata.domain.tourspot.service.TourSpotGeoCodeService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/tourspot")
//@RequiredArgsConstructor
//public class TourSpotGeoCodeController {
//
//    private final TourSpotGeoCodeService tourSpotGeoCodeService;
//
//    @PostMapping("/geocode")
//    public ResponseEntity<String> fetchAndInsertTourSpotData() {
//        tourSpotGeoCodeService.processTourSpots();
//        return ResponseEntity.ok("✅ 관광지 데이터 처리 완료");
//    }
//}
