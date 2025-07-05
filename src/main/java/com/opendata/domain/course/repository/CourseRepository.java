package com.opendata.domain.course.repository;


import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.repository.custom.CustomCourseRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, CustomCourseRepository {

}
