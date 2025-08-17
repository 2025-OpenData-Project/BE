package com.opendata.domain.course.controller;


import com.opendata.docs.CourseControllerDocs;
import com.opendata.domain.course.dto.response.CourseResponse;
import com.opendata.domain.course.service.CourseService;

import com.opendata.global.response.ApiResponse;

import com.opendata.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController implements CourseControllerDocs {
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>> findCourses(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam String tourspot
            ){
        return ResponseEntity.ok(ApiResponse.onSuccess(
                courseService.recommendCourses(lat, lon, startTime, endTime, tourspot)));
    }

    @PostMapping("/like/{courseId}")
    public ResponseEntity<ApiResponse<Void>> postCourseLike(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable String courseId){
        courseService.likeCourse(courseId,customUserDetails);
        return ResponseEntity.ok(ApiResponse.onSuccessVoid());
    }
//
//    @GetMapping("/spec/{courseId}")
//    public ResponseEntity<ApiResponse<CourseSpecResponse>> getCourseSpec(@PathVariable Long courseId){
//        return ResponseEntity.ok(ApiResponse.onSuccess(courseService.findCourseSpec(courseId)));
//    }
}
