package com.opendata.domain.course.repository.custom;

import com.opendata.domain.apidata.entity.Area;

import java.util.List;

public interface CustomCourseRepository {
    List<Area> findQuietAreas();
}
