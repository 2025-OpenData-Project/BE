package com.opendata.domain.course.mapper;


import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.entity.CourseComponent;
import com.opendata.domain.tourspot.dto.FilteredTourSpot;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);


    @Mapping(source = "tourSpot", target = "tourSpot")
    @Mapping(source = "tourspotTm", target = "tourspotTm")
    CourseComponent toEntity(FilteredTourSpot dto);
}
