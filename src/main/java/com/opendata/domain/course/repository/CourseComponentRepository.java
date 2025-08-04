package com.opendata.domain.course.repository;

import com.opendata.domain.course.entity.CourseComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseComponentRepository extends JpaRepository<CourseComponent, Long> {
}
