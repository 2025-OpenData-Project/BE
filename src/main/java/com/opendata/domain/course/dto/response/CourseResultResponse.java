package com.opendata.domain.course.dto.response;

import com.opendata.domain.tourspot.dto.AreaComponentDto;
import lombok.Builder;

import java.util.List;

@Builder
public record CourseResultResponse(
        List<GeneratedCourse> courses,
        int courseCount,
        String startTime,
        String endTime
) {
    public static CourseResultResponse from(List<List<AreaComponentDto>> rawCourses, int count, String start, String end) {
        List<GeneratedCourse> transformed = rawCourses.stream()
                .map(GeneratedCourse::of)
                .toList();
        return new CourseResultResponse(transformed, count, start, end);
    }
}
