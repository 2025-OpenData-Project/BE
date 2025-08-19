package com.opendata.domain.course.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opendata.domain.course.dto.response.CourseComponentDto;
import com.opendata.domain.course.dto.response.CourseResponse;

import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.entity.CourseComponent;
import com.opendata.domain.course.exception.CourseNotFoundException;
import com.opendata.domain.course.mapper.CourseComponentMapper;
import com.opendata.domain.course.message.CourseMessages;
import com.opendata.domain.course.repository.CourseComponentRepository;
import com.opendata.domain.tourspot.dto.FilteredTourSpot;

import com.opendata.domain.course.repository.CourseRepository;
import com.opendata.domain.course.util.CourseUtil;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotFutureCongestion;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import com.opendata.domain.tourspot.mapper.CourseResponseMapper;
import com.opendata.domain.tourspot.repository.FutureCongestionRepository;
import com.opendata.domain.tourspot.repository.TourSpotRepository;

import com.opendata.domain.user.entity.User;
import com.opendata.domain.user.repository.UserRepository;


import com.opendata.global.security.CustomUserDetails;
import com.opendata.global.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseComponentRepository courseComponentRepository;
    private final CourseComponentMapper courseComponentMapper;
    private final TourSpotRepository tourSpotRepository;
    private final FutureCongestionRepository futureCongestionRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private final CourseResponseMapper courseResponseMapper;



    public List<CourseResponse> recommendCourses(double userLat, double userLon, String startTime, String endTime, String tourspot) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<FilteredTourSpot> candidates = getFilteredCandidates(userLat, userLon, startTime, endTime, tourspot, formatter);
        List<List<CourseComponent>> resultCourses = new ArrayList<>();
        List<CourseResponse> courseResponses = new ArrayList<>();

        Set<Long> usedAreaIds = new HashSet<>();

        for (FilteredTourSpot startArea : candidates) {
            List<CourseComponent> course = buildCourse(startArea, candidates, userLat, userLon, formatter);

            if (course.size() >= 3 && !isDuplicateCourse(course, resultCourses)) {
                resultCourses.add(course);
            }
            course.forEach(a -> usedAreaIds.add(a.getId()));
            if (resultCourses.size() >= 5) break;
        }

        List<List<CourseComponentDto>> responseCourses = resultCourses.stream()
                .map(course -> course.stream()
                        .map(component -> {
                            Long spotId = component.getTourSpot().getTourspotId();
                            LocalDateTime time = component.getTourspotTm();

                            Optional<TourSpotFutureCongestion> congestion =
                                    futureCongestionRepository.findByTourSpotIdAndFcstTime(spotId, time.format(formatter));

                            CongestionLevel level = congestion.get().getCongestionLvl();

                            return CourseComponentDto.from(component, level);
                        })
                        .toList()
                )
                .toList();

        responseCourses.forEach(
                singleCourse -> {
                    String tempCourseId = "tempCourse:" + UUID.randomUUID();
                    courseResponses.add(courseResponseMapper.toResponse(tempCourseId, singleCourse));
                    redisTemplate.opsForValue().set(tempCourseId, singleCourse, Duration.ofMinutes(30));
                }
        );

        return courseResponses;
    }

    private boolean isDuplicateCourse(List<CourseComponent> newCourse, List<List<CourseComponent>> existingCourses) {
        Set<Long> newCourseIds = newCourse.stream()
                .map(CourseComponent::getId)
                .collect(Collectors.toSet());

        for (List<CourseComponent> existing : existingCourses) {
            Set<Long> existingIds = existing.stream()
                    .map(CourseComponent::getId)
                    .collect(Collectors.toSet());

            // 두 코스의 ID 교집합 개수
            long intersection = newCourseIds.stream().filter(existingIds::contains).count();

            // 교집합 비율이 너무 높으면 (ex. 80% 이상) 중복으로 판단
            double overlapRatio = (double) intersection / Math.min(newCourseIds.size(), existingIds.size());

            if (overlapRatio >= 0.8 && intersection >= 3) {
                // 80% 이상 겹치고, 3개 이상 겹치는 경우만 중복으로 간주
                return true;
            }
        }
        return false;
    }

    private List<FilteredTourSpot> getFilteredCandidates(double userLat, double userLon, String startTime, String endTime, String tourspot, DateTimeFormatter formatter) {
        return tourSpotRepository.findAll().stream()
                .flatMap(area -> area.getFutureCongestions().stream()
                        .filter(f -> isInTimeRange(f.getFcstTime(), startTime, endTime))
                        .filter(f -> CongestionLevel.checkIsCourseComponent(f.getCongestionLvl().getCongestionLabel()))
                        .map(f -> new FilteredTourSpot(
                                area,
                                LocalDateTime.parse(f.getFcstTime(), formatter)
                        ))
                )
                .filter(a -> CourseUtil.calculateDistance(userLat, userLon, a.tourSpot().getAddress().getLatitude(), a.tourSpot().getAddress().getLongitude()) <= 15.0)
                .toList();
    }

    private List<CourseComponent> buildCourse(FilteredTourSpot startArea, List<FilteredTourSpot> candidates,
                                               double userLat, double userLon, DateTimeFormatter formatter) {

        Set<TourSpot> visited = new HashSet<>();
        visited.add(startArea.tourSpot());

        List<CourseComponent> course = new ArrayList<>();
        FilteredTourSpot current = startArea;
        LocalDateTime currentTime = current.tourspotTm();
        course.add(courseComponentMapper.toEntity(current));

        double dx = startArea.tourSpot().getAddress().getLatitude() - userLat;
        double dy = startArea.tourSpot().getAddress().getLongitude() - userLon;

        while (course.size() < 5) {
            FilteredTourSpot next = findNextAreaDirectional(current, candidates, visited, currentTime, dx, dy);

            if (next == null) break;
            visited.add(next.tourSpot());
            currentTime = next.tourspotTm();
            course.add(courseComponentMapper.toEntity(next));
            current = next;
        }
        return course;
    }


    private boolean isInTimeRange(String time, String start, String end) {
        return time.compareTo(start) >= 0 && time.compareTo(end) <= 0;
    }

    private FilteredTourSpot findNextAreaDirectional(FilteredTourSpot from, List<FilteredTourSpot> candidates,
                                                     Set<TourSpot> visited, LocalDateTime currentTime,
                                                     double dx, double dy) {

        return candidates.stream()
                .peek(a -> {
                    if (visited.contains(a.tourSpot())) {
                        log.debug("방문된 장소 제외: {}", a.tourSpot().getTourspotNm());
                    } else if (a.tourspotTm().isBefore(currentTime.plusMinutes(30)) ||
                            a.tourspotTm().isAfter(currentTime.plusHours(3))) {
                        log.debug("시간 조건 불일치: {}", a.tourspotTm());
                    } else {
                        double distance = CourseUtil.calculateDistance(
                                from.tourSpot().getAddress().getLatitude(), from.tourSpot().getAddress().getLongitude(),
                                a.tourSpot().getAddress().getLatitude(), a.tourSpot().getAddress().getLongitude()
                        );
                        if (distance > 30.0) {
                            log.debug("거리 초과: {} km, 장소: {}", distance, a.tourSpot().getTourspotNm());
                        } else {
                            log.debug("후보 통과: {}", a.tourSpot().getTourspotNm());
                        }
                    }
                })
                .filter(a -> !visited.contains(a.tourSpot()))
                .filter(a -> a.tourspotTm().isAfter(currentTime.plusMinutes(30)))
                .filter(a -> a.tourspotTm().isBefore(currentTime.plusHours(3)))
                .filter(a -> CourseUtil.calculateDistance(
                        from.tourSpot().getAddress().getLatitude(), from.tourSpot().getAddress().getLongitude(),
                        a.tourSpot().getAddress().getLatitude(), a.tourSpot().getAddress().getLongitude()
                ) <= 15.0)
                .min(Comparator.comparingDouble(a -> CourseUtil.calculateDistance(
                        from.tourSpot().getAddress().getLatitude(), from.tourSpot().getAddress().getLongitude(),
                        a.tourSpot().getAddress().getLatitude(), a.tourSpot().getAddress().getLongitude()
                )))
                .orElse(null);
    }

    @Transactional
    public void likeCourse(String courseId, CustomUserDetails customUserDetails) {
        String email = customUserDetails.getUserEmail();
        User user = userRepository.findUserByEmail(email);
        ObjectMapper objectMapper = new ObjectMapper();

        List<?> rawList = (List<?>) redisTemplate.opsForValue().get("tempCourse:" + courseId);
        if (rawList == null || rawList.isEmpty()){
            throw new CourseNotFoundException(CourseMessages.COURSE_SAVE_EXPIRED);
        }

        List<CourseComponentDto> tempCourse = objectMapper.convertValue(
                rawList,
                new TypeReference<List<CourseComponentDto>>() {}
        );

        Course course = Course.builder()
                .uuid(courseId)
                .user(user)
                .build();

        courseRepository.save(course);
        for(int i = 0; i < tempCourse.size(); i++){
            CourseComponentDto courseComponentDto = tempCourse.get(i);
            if (i == 0){
                course.assignStartDtm(DateUtil.parseTime(courseComponentDto.time()));
            }
            if (i == tempCourse.size()-1){
                course.assignEndDtm(DateUtil.parseTime(courseComponentDto.time()));
            }
            TourSpot tourSpot = tourSpotRepository.findById(courseComponentDto.tourspotId()).orElseThrow();
            CourseComponent courseComponent = courseComponentMapper.toEntity(tourSpot, DateUtil.parseTime(courseComponentDto.time()), course);

            courseComponentRepository.save(courseComponent);
        }
    }

    public CourseResponse fetchCourseDetail(String courseId){
        ObjectMapper objectMapper = new ObjectMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<?> rawList = (List<?>) redisTemplate.opsForValue().get("tempCourse:" + courseId);
        if (rawList == null || rawList.isEmpty()){
            List<CourseComponentDto> courseComponentDtoList = new ArrayList<>();
            Course course = courseRepository.findByUuid(courseId).orElseThrow();
            course.getCourseComponents().forEach(
                    courseComponent -> {
                        Long spotId = courseComponent.getTourSpot().getTourspotId();
                        LocalDateTime time = courseComponent.getTourspotTm();

                        Optional<TourSpotFutureCongestion> congestion =
                                futureCongestionRepository.findByTourSpotIdAndFcstTime(spotId, time.format(formatter));

                        CongestionLevel level = congestion.get().getCongestionLvl();
                        courseComponentDtoList.add(CourseComponentDto.from(courseComponent, level));

                    }
            );
            return new CourseResponse(courseId, courseComponentDtoList);
        }
        List<CourseComponentDto> tempCourse = objectMapper.convertValue(
                rawList,
                new TypeReference<List<CourseComponentDto>>() {}
        );
        return new CourseResponse(courseId, tempCourse);
    }


//
//    public CourseSpecResponse findCourseSpec(Long courseId){
//        return CourseSpecResponse.from(courseRepository.findById(courseId).orElseThrow());
//    }
}