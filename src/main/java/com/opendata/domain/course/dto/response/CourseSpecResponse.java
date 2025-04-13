package com.opendata.domain.course.dto.response;

import com.opendata.domain.apidata.dto.AreaComponentDto;
import com.opendata.domain.course.entity.Course;

import java.util.List;

public record CourseSpecResponse(
        List<AreaComponentDto> areas,
        String startTime,
        String endTime
) {
    public static CourseSpecResponse from(Course course){
        return new CourseSpecResponse(
            course.getPlaces(), course.getStartTime(), course.getEndTime()
        );
    }
}
