package com.opendata.domain.mypage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.opendata.domain.tourspot.dto.AddressDto;
import com.opendata.domain.tourspot.dto.TourSpotEventDto;
import com.opendata.domain.tourspot.dto.TourSpotTagDto;
import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotComponent;
import com.opendata.domain.tourspot.entity.TourSpotCurrentCongestion;
import com.opendata.domain.tourspot.mapper.CourseResponseMapper;
import com.opendata.domain.tourspot.mapper.TourSpotDetailMapper;
import com.opendata.domain.tourspot.repository.CurrentCongestionRepository;
import com.opendata.domain.tourspot.repository.TourSpotComponentRepository;
import com.opendata.domain.tourspot.repository.TourSpotRepository;
import com.opendata.domain.tourspot.service.TourSpotService;
import com.opendata.global.response.exception.GlobalException;
import com.opendata.global.response.status.ErrorStatus;
import com.opendata.global.security.CustomUserDetails;
import com.opendata.global.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final CourseRepository courseRepository;
    private final TourSpotComponentRepository tourSpotComponentRepository;

    private final TourSpotService tourSpotService;

    private final CourseHistoryMapper courseHistoryMapper;
    private final TourSpotDetailMapper tourSpotDetailMapper;

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
    public void saveUserTourSpot(CustomUserDetails customUserDetails,Long tourSpotId)
    {
        Long userId = customUserDetails.getUserId();
        if (tourSpotComponentRepository.existsByUserIdAndTourSpotId(userId, tourSpotId)) {
            throw new GlobalException(ErrorStatus.TOURSPOT_ALREADY_EXISTS);
        }
        if (tourSpotComponentRepository.countByUserId(userId) >= 5) {
            throw new GlobalException(ErrorStatus.TOURSPOT_EXCEEDS);
        }
        TourSpotComponent tourSpotComponent = TourSpotComponent.toTourSpotComponent(userId, tourSpotId);
        tourSpotComponentRepository.save(tourSpotComponent);
    }

    public List<TourSpotDetailResponse> getTourSpotDetail(CustomUserDetails customUserDetails)
    {
        Long userId= customUserDetails.getUserId();
        return tourSpotComponentRepository.findAllByUserId(userId).stream()
                .map(tourSpotComponent -> {
                    try {
                        return tourSpotService.combineTourSpotDetail(tourSpotComponent.getTourSpotId());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                })
                .collect(Collectors.toList());

    }
}
