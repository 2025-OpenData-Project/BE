package com.opendata.domain.course.repository.custom;


import com.opendata.domain.course.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CustomCourseRepository {
    List<Course> findAllByUserId(Long userId);
    Optional<Course> findByUuid(String uuid);
}
