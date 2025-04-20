package com.opendata.domain.course.controller;


import com.opendata.domain.course.dto.response.CourseLikeRequest;
import com.opendata.domain.course.dto.response.CourseResultResponse;
import com.opendata.domain.course.dto.response.CourseSpecResponse;
import com.opendata.domain.course.entity.Course;
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
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<CourseResultResponse>> findCourses(
            //@AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam String tourspot
            ){
        return ResponseEntity.ok(ApiResponse.onSuccess(
                courseService.recommendCourses(lat, lon, startTime, endTime, tourspot)));
    }

    @PostMapping("/like")
    public ResponseEntity<ApiResponse<Course>> postCourseLike(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CourseLikeRequest request){
        return ResponseEntity.ok(ApiResponse.onSuccess(courseService.likeCourse(customUserDetails, request)));
    }

    @GetMapping("/spec/{courseId}")
    public ResponseEntity<ApiResponse<CourseSpecResponse>> getCourseSpec(@PathVariable String courseId){
        return ResponseEntity.ok(ApiResponse.onSuccess(courseService.findCourseSpec(courseId)));
    }
}
