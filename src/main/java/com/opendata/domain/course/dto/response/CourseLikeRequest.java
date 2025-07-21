package com.opendata.domain.course.dto.response;

import com.opendata.domain.tourspot.dto.AreaComponentDto;


import java.util.List;

public record CourseLikeRequest(
        List<AreaComponentDto> areas,
        Integer courseNumber,
        String startTime,
        String endTime,
        boolean isActive
) {
}
