package com.opendata.domain.course.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

public record CourseComponentHistoryDto(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        String tourSpotTime,
        String tourSpotName,
        Long tourspotId,
        Double lat, Double lon)
{

}
