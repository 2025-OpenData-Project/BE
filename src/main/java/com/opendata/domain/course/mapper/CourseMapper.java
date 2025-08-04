package com.opendata.domain.course.mapper;

import com.opendata.domain.course.dto.response.CourseComponentDto;
import com.opendata.domain.course.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CourseMapper {

    Course toEntity(CourseComponentDto courseComponentDto);
}
