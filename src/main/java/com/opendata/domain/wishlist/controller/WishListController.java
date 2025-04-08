package com.opendata.domain.wishlist.controller;

import com.opendata.domain.course.dto.response.CourseResultResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.wishlist.service.WishListService;
import com.opendata.global.jwt.JwtUtil;
import com.opendata.global.response.ApiResponse;
import com.opendata.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishListController
{
    private final WishListService wishListService;


    @GetMapping("/get/one")
    public ResponseEntity<ApiResponse<Course>> findCourse(@RequestParam String courseId)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(wishListService.getCourseOne(courseId)));
    }
    @GetMapping("/get/list")
    public ResponseEntity<ApiResponse<List<Course>>> findCourses(@RequestHeader("Authorization")String acessToken)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(wishListService.getCourses(acessToken)));

    }
}
