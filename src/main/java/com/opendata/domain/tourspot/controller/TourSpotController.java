package com.opendata.domain.tourspot.controller;

import com.opendata.domain.tourspot.dto.AreaCongestionDto;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import com.opendata.domain.tourspot.service.TourSpotService;
import com.opendata.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/area")
public class TourSpotController
{
    private final TourSpotService tourSpotService;

    @GetMapping
    public ResponseEntity<Void> getArea()
    {
        tourSpotService.fetchAllAreaAndSave();
        return ResponseEntity.ok().build();
    }


//    @GetMapping("/list")
//    public ResponseEntity<ApiResponse<List<AreaCongestionDto>>> getAreaListWithCongestion(){
//        return ResponseEntity.ok(ApiResponse.onSuccess(tourSpotService.fetchAndConvertAreaCongestionDto()));
//    }
}
