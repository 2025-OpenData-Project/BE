package com.opendata.domain.mypage.service;

import com.opendata.domain.course.dto.response.CourseComponentDto;
import com.opendata.domain.course.dto.response.CourseComponentHistoryDto;
import com.opendata.domain.course.dto.response.CourseHistoryResponse;
import com.opendata.domain.course.dto.response.CourseResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.entity.CourseComponent;
import com.opendata.domain.course.mapper.CourseComponentMapper;
import com.opendata.domain.course.mapper.CourseHistoryMapper;
import com.opendata.domain.course.repository.CourseComponentRepository;
import com.opendata.domain.course.repository.CourseRepository;
import com.opendata.domain.course.service.CourseService;
import com.opendata.domain.tourspot.mapper.CourseResponseMapper;
import com.opendata.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final CourseRepository courseRepository;
    private final CourseComponentRepository courseComponentRepository;

    private final CourseHistoryMapper courseHistoryMapper;

    public List<CourseHistoryResponse> getCourses(CustomUserDetails customUserDetails)
    {
        Long userId = customUserDetails.getUserId();

        return courseRepository.findAllByUserId(userId).stream()
                .map(course -> {
                    var components = Optional.ofNullable(course.getCourseComponents())
                            .orElseGet(Collections::emptyList);

                    var history = components.stream()
                            .map(courseHistoryMapper::toHistoryDto)
                            .toList();

                    return courseHistoryMapper.toHistoryResponse(course, history);
                })
                .toList();



    }
}
