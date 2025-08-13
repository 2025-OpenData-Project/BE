package com.opendata.domain.mypage.controller;

import com.opendata.domain.course.dto.response.CourseHistoryResponse;
import com.opendata.domain.course.dto.response.CourseResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.mypage.service.MypageService;
import com.opendata.global.response.ApiResponse;
import com.opendata.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
