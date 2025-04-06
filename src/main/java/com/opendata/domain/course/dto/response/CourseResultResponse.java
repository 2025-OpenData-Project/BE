package com.opendata.domain.course.dto.response;

import com.opendata.domain.apidata.dto.AreaComponentDto;
import com.opendata.domain.apidata.entity.Area;
import com.opendata.domain.course.entity.Course;
import lombok.Builder;

import java.util.List;

@Builder
public record CourseResultResponse(
        List<List<AreaComponentDto>> areas,
        Integer courseNumber,
        String startTime,
        String endTime
) {
    public static CourseResultResponse from(List<List<AreaComponentDto>> areas, Integer courseNumber,
                                            String startTime, String endTime){
        return CourseResultResponse.builder()
                .areas(areas)
                .courseNumber(courseNumber)
                .startTime(startTime)
                .endTime(endTime)
                .build();

    }
}
