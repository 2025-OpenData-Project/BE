package com.opendata.domain.tourspot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opendata.docs.TourSpotControllerDocs;
import com.opendata.domain.tourspot.dto.AreaCongestionDto;
import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import com.opendata.domain.tourspot.service.TourSpotService;
import com.opendata.global.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tourspot")
public class TourSpotController implements TourSpotControllerDocs {
    private final TourSpotService tourSpotService;

    @GetMapping
    public ResponseEntity<Void> getArea() {
        tourSpotService.fetchAllAreaAndSave();
        return ResponseEntity.ok().build();
    }


    @GetMapping("/related")
    public ResponseEntity<Void> getRelated() {
        tourSpotService.saveRelatedTourspot();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tourspotId}")
    public ResponseEntity<ApiResponse<TourSpotDetailResponse>> getTourSpotDetail(@PathVariable("tourspotId") Long tourspotId) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.onSuccess(tourSpotService.combineTourSpotDetail(tourspotId)));
    }



//    @GetMapping("/list")
//    public ResponseEntity<ApiResponse<List<AreaCongestionDto>>> getAreaListWithCongestion(){
//        return ResponseEntity.ok(ApiResponse.onSuccess(tourSpotService.fetchAndConvertAreaCongestionDto()));
//    }
}
