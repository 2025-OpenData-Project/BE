package com.opendata.domain.tourspot.mapper;

import com.opendata.domain.course.dto.response.CourseComponentDto;
import com.opendata.domain.course.dto.response.CourseResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import jakarta.persistence.ManyToOne;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CourseResponseMapper {

    CourseResponse toResponse(String courseId, List<CourseComponentDto> courseComponentDtoList);
}
