package com.opendata.domain.wishlist.service;


import com.opendata.domain.course.dto.response.CourseResultResponse;
import com.opendata.domain.course.dto.response.CourseSpecResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.repository.CourseRepository;
import com.opendata.global.jwt.JwtUtil;
import com.opendata.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishListService
{
    private final CourseRepository courseRepository;
    private final JwtUtil jwtUtil;

    public Course getCourseOne(String courseId)
    {
        return courseRepository.findById(courseId).get();
    }

    public List<Course> getCourses(CustomUserDetails userDetails)
    {
        String userId=userDetails.getUserId();
        log.info("userId:{}",userId);
        return courseRepository.findCoursesByUserId(userId);
    }

    public String deleteCourse(String courseId)
    {
        Course course=courseRepository.findById(courseId).get();
        course.setLike(false);
        courseRepository.save(course);
        return courseId;
    }

    @Transactional
    public Course selectCourse(String courseId)
    {
        Course course=courseRepository.findById(courseId).get();
        Course activeCourse= courseRepository.findCourseByIdWithActive();
        activeCourse.setActive(false);
        course.setActive(true);
        courseRepository.save(activeCourse);
        courseRepository.save(course);
        return course;

    }
    public CourseSpecResponse shareCourse(String courseId)
    {
        Course course=courseRepository.findById(courseId).get();
        return CourseSpecResponse.from(course);
    }



}
