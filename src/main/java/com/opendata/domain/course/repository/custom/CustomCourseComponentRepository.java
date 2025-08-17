package com.opendata.domain.course.repository.custom;

import com.opendata.domain.course.entity.CourseComponent;

import java.util.List;

public interface CustomCourseComponentRepository
{
    List<CourseComponent> findAllByCourseId(Long courseId);
}
