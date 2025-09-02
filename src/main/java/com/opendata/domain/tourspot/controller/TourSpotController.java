package com.opendata.domain.tourspot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opendata.docs.TourSpotControllerDocs;

import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import com.opendata.domain.tourspot.dto.response.TourSpotMetaResponse;
import com.opendata.domain.tourspot.dto.response.TourSpotRelatedResponse;

import com.opendata.domain.tourspot.service.TourSpotRelatedService;
import com.opendata.domain.tourspot.service.TourSpotService;
import com.opendata.global.response.ApiResponse;

import com.opendata.global.response.PageResponse;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/tourspot")
public class TourSpotController implements TourSpotControllerDocs {
    private final TourSpotService tourSpotService;
    private final TourSpotRelatedService tourSpotRelatedService;

    @GetMapping
    public ResponseEntity<Void> getArea() {
        tourSpotService.fetchAllAreaAndSave();
        return ResponseEntity.ok().build();
    }


//    @GetMapping("/related")
//    public ResponseEntity<Void> getRelated() {
//        tourSpotService.saveRelatedTourspot();
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/{tourspotId}")
    public ResponseEntity<ApiResponse<TourSpotDetailResponse>> getTourSpotDetail(@PathVariable("tourspotId") Long tourspotId) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.onSuccess(tourSpotService.combineTourSpotDetail(tourspotId)));
    }

    @GetMapping("/meta")
    public ResponseEntity<ApiResponse<PageResponse<List<TourSpotMetaResponse>>>> getTourSpotMeta(@RequestParam(defaultValue = "1") @Min(value = 1) int page, @RequestParam(defaultValue = "5") @Min(value = 1) int size) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseEntity.ok(ApiResponse.onSuccess(tourSpotService.combineTourSpotMeta(pageable)));
    }

    @GetMapping("/related/{addressId}")
    public ResponseEntity<ApiResponse<List<TourSpotRelatedResponse>>> getRelatedTourSpotDetail(@PathVariable("addressId") Long addressId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(tourSpotRelatedService.fetchRelatedTourSpots(addressId)));
    }



//    @GetMapping("/list")
//    public ResponseEntity<ApiResponse<List<AreaCongestionDto>>> getAreaListWithCongestion(){
//        return ResponseEntity.ok(ApiResponse.onSuccess(tourSpotService.fetchAndConvertAreaCongestionDto()));
//    }
}
