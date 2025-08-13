package com.opendata.domain.course.repository;

import com.opendata.domain.course.entity.CourseComponent;
import com.opendata.domain.course.repository.custom.CustomCourseComponentRepository;
import com.opendata.domain.course.repository.custom.CustomCourseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseComponentRepository extends JpaRepository<CourseComponent, Long> , CustomCourseComponentRepository {

}
