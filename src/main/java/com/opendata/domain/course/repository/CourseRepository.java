package com.opendata.domain.course.repository;

import com.opendata.domain.apidata.entity.Area;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.repository.custom.CustomCourseRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<Course, String>, CustomCourseRepository {

}
