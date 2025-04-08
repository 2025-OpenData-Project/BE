package com.opendata.domain.wishlist.service;


import com.opendata.domain.course.dto.response.CourseResultResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.repository.CourseRepository;
import com.opendata.global.jwt.JwtUtil;
import com.opendata.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService
{
    private final CourseRepository courseRepository;
    private final JwtUtil jwtUtil;

    public Course getCourseOne(String courseId)
    {
        return courseRepository.findById(courseId).get();
    }

    public List<Course> getCourses(String acessToken)
    {
        String userId=jwtUtil.getId(acessToken);
        return courseRepository.findCoursesByUserId(userId);
    }

    public String deleteCourse(String courseId)
    {
        Course course=courseRepository.findById(courseId).get();
        course.setFavorite(false);
        courseRepository.save(course);
        return courseId;
    }
}
