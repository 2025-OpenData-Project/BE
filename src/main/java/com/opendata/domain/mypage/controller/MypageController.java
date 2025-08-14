package com.opendata.domain.mypage.controller;

import com.opendata.domain.course.dto.response.CourseHistoryResponse;
import com.opendata.domain.mypage.service.MypageService;
import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.global.response.ApiResponse;
import com.opendata.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController
{
    private final MypageService mypageService;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<CourseHistoryResponse>>> findCourses(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.getCourses(customUserDetails)));
    }
    @PostMapping("/preferences")
    public ResponseEntity<ApiResponse<Void>> updateTourSpot(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long tourSpotId
    ){
        mypageService.saveUserTourSpot(customUserDetails,tourSpotId);
        return ResponseEntity.ok(ApiResponse.onSuccessVoid());
    }
    @GetMapping("/preferences")
    public ResponseEntity<ApiResponse<List<TourSpotDetailResponse>>> findTourSpot(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.getTourSpotDetail(customUserDetails)));
    }

}
