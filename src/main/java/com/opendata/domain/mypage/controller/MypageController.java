package com.opendata.domain.mypage.controller;

import com.opendata.docs.MyPageControllerDocs;
import com.opendata.domain.course.dto.response.CourseHistoryResponse;
import com.opendata.domain.mypage.service.MypageService;
import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.user.dto.UserResponse;
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
public class MypageController implements MyPageControllerDocs
{
    private final MypageService mypageService;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<CourseHistoryResponse>>> findCourses(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.getCourses(customUserDetails)));
    }
    @PostMapping("/preferences/{tourspotId}")
    public ResponseEntity<ApiResponse<Void>> updateTourSpot(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("tourspotId") Long tourSpotId
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
    @DeleteMapping("/preferences/{tourSpotId}")
    public ResponseEntity<ApiResponse<Void>> deleteTourSpot(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("tourspotId") Long tourSpotId
    ){
        mypageService.deleteTourSpot(customUserDetails,tourSpotId);
        return ResponseEntity.ok(ApiResponse.onSuccessVoid());
    }
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> findUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.getUser(customUserDetails)));
    }

    @GetMapping("/preferences/check/{tourSpotId}")
    public ResponseEntity<ApiResponse<Boolean>> CheckTourSpot(
        @PathVariable("tourspotId") Long tourSpotId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(ApiResponse.onSuccess(mypageService.isPreferenceTourSpot(customUserDetails, tourSpotId)));
    }






}
