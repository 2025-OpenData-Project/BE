package com.opendata.domain.course.dto.response;

import com.opendata.domain.tourspot.dto.AreaComponentDto;
import lombok.Builder;

import java.util.List;


public record CourseResultResponse(
        List<GeneratedCourse> courses,
        int courseCount,
        String startTime,
        String endTime
) {
}
