package com.opendata.domain.course.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.opendata.domain.course.entity.CourseComponent;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;


import java.time.LocalDateTime;

public record CourseComponentDto(
        String tourspotNm, String congestionLevel,
                                      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                      LocalDateTime time, Double lat, Double lon) {
    public static CourseComponentDto from(CourseComponent c, CongestionLevel level) {
        TourSpot tourSpot = c.getTourSpot();
        return new CourseComponentDto(tourSpot.getTourspotNm(),level.getCongestionLabel() ,c.getTourspotTm(),
                tourSpot.getAddress().getLatitude(), tourSpot.getAddress().getLongitude());
    }
}
