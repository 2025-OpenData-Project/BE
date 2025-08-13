package com.opendata.domain.course.repository.custom;


import com.opendata.domain.course.entity.Course;

import java.util.List;

public interface CustomCourseRepository {
    List<Course> findAllByUserId(Long userId);
}
