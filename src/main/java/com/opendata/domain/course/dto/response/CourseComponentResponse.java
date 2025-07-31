package com.opendata.domain.course.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysql.cj.util.TimeUtil;
import com.opendata.domain.course.entity.CourseComponent;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.global.util.DateUtil;

import java.time.LocalDateTime;

public record CourseComponentResponse(String tourspotNm,
                                      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                      LocalDateTime time, Double lat, Double lon) {
    public static CourseComponentResponse from(CourseComponent c) {
        TourSpot tourSpot = c.getTourSpot();
        return new CourseComponentResponse(tourSpot.getTourspotNm(), c.getTourspotTm(),
                tourSpot.getAddress().getLatitude(), tourSpot.getAddress().getLongitude());
    }
}
