package com.opendata.domain.course.dto.response;



import java.util.List;


public record CourseResultResponse(
        List<GeneratedCourse> courses,
        int courseCount,
        String startTime,
        String endTime
) {
}
