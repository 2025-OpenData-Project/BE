package com.opendata.domain.course.mapper;


import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.entity.CourseComponent;
import com.opendata.domain.tourspot.dto.FilteredTourSpot;

import com.opendata.domain.tourspot.entity.TourSpot;
import org.mapstruct.*;

import java.time.LocalDateTime;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CourseComponentMapper {


    @Mapping(source = "tourSpot", target = "tourSpot")
    @Mapping(source = "tourspotTm", target = "tourspotTm")
    CourseComponent toEntity(FilteredTourSpot dto);


    @Mapping(source = "tourSpot", target = "tourSpot")
    @Mapping(source = "tourspotTm", target = "tourspotTm")
    CourseComponent toEntity(TourSpot tourSpot, LocalDateTime tourspotTm, @Context Course course);

    @AfterMapping
    default void assignCourse(@MappingTarget CourseComponent courseComponent,
                              @Context Course course){
        courseComponent.assignCourse(course);
    }
}
