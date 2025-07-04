//package com.opendata.domain.course.dto.response;
//
//import com.opendata.domain.tourspot.dto.AreaComponentDto;
//import com.opendata.domain.course.entity.Course;
//import lombok.Builder;
//
//import java.util.List;
//
//@Builder
//public record CourseLikeRequest(
//        List<AreaComponentDto> areas,
//        Integer courseNumber,
//        String startTime,
//        String endTime,
//        boolean isActive
//) {
//    public Course from(String id){
//        return Course.builder()
//                .userId(id)
//                .places(areas)
//                .placeCount(this.courseNumber)
//                .startTime(this.startTime)
//                .endTime(this.endTime)
//                .isLike(true)
//                .isActive(isActive)
//                .build();
//    }
//}
