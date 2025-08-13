package com.opendata.domain.course.mapper;

import com.opendata.domain.course.dto.response.CourseComponentHistoryDto;
import com.opendata.domain.course.dto.response.CourseHistoryResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.entity.CourseComponent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CourseHistoryMapper {

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "startDtm", source = "course.startDtm")
    @Mapping(target = "endDtm", source = "course.endDtm")
    @Mapping(target = "history", source = "components")
    CourseHistoryResponse toHistoryResponse(Course course, List<CourseComponentHistoryDto> components);


    @Mapping(target = "tourSpotTime", source = "tourspotTm", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "tourSpotName", source = "tourSpot.tourspotNm")
    @Mapping(target = "tourspotId", source = "tourSpot.tourspotId")
    @Mapping(target = "lat", source = "tourSpot.address.latitude")
    @Mapping(target = "lon", source = "tourSpot.address.longitude")
    CourseComponentHistoryDto toHistoryDto(CourseComponent component);


}
