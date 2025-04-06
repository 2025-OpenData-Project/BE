package com.opendata.domain.course.controller;

import com.opendata.domain.apidata.entity.Area;
import com.opendata.domain.course.dto.response.CourseResultResponse;
import com.opendata.domain.course.service.CourseService;
import com.opendata.domain.course.util.CourseUtil;
import com.opendata.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<CourseResultResponse>>> findCourses(
            double lat, double lon, String startTime, String endTime){
        return ResponseEntity.ok(ApiResponse.onSuccess(courseService.findAllCourses(lat, lon, startTime, endTime)));
    }
}
