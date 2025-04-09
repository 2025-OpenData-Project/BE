package com.opendata.domain.course.repository.custom;

import com.opendata.domain.apidata.entity.Area;
import com.opendata.domain.course.entity.Course;

import java.util.List;

public interface CustomCourseRepository {
    List<Area> findQuietAreas();
    List<Course> findCoursesByUserId(String userId);
    Course findCourseByIdWithActive();
}
