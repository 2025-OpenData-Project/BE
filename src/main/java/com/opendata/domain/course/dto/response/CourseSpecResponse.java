package com.opendata.domain.course.dto.response;

import com.opendata.domain.tourspot.dto.AreaComponentDto;
import com.opendata.domain.course.entity.Course;

import java.util.List;

public record CourseSpecResponse(
        List<AreaComponentDto> areas,
        String startTime,
        String endTime
) {
}
