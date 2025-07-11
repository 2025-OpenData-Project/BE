package com.opendata.domain.course.dto.response;

import com.opendata.domain.course.entity.CourseComponent;

import java.time.LocalDateTime;

public record CourseComponentResponse(String tourspotNm, LocalDateTime time) {
    public static CourseComponentResponse from(CourseComponent c) {
        return new CourseComponentResponse(c.getTourSpot().getTourspotNm(), c.getTourspotTm());
    }
}
