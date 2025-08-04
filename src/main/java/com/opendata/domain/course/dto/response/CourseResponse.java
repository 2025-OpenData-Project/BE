package com.opendata.domain.course.dto.response;

import java.util.List;

public record CourseResponse
        (String courseId, List<CourseComponentDto> courseComponentDtoList)
{
}
