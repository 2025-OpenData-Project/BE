package com.opendata.domain.course.service;

import com.opendata.domain.course.dto.response.CourseComponentResponse;
import com.opendata.domain.course.entity.CourseComponent;
import com.opendata.domain.course.mapper.CourseMapper;
import com.opendata.domain.tourspot.dto.AreaComponentDto;
import com.opendata.domain.tourspot.dto.FilteredTourSpot;

import com.opendata.domain.course.dto.response.CourseLikeRequest;
import com.opendata.domain.course.dto.response.CourseResultResponse;
import com.opendata.domain.course.dto.response.CourseSpecResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.repository.CourseRepository;
import com.opendata.domain.course.util.CourseUtil;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import com.opendata.domain.tourspot.repository.TourSpotRepository;
import com.opendata.domain.user.entity.User;
import com.opendata.domain.user.repository.UserRepository;

import com.opendata.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TourSpotRepository tourSpotRepository;
    private final UserRepository userRepository;



    public List<List<CourseComponentResponse>> recommendCourses(double userLat, double userLon, String startTime, String endTime, String tourspot) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<FilteredTourSpot> candidates = getFilteredCandidates(userLat, userLon, startTime, endTime, tourspot, formatter);

        List<List<CourseComponent>> resultCourses = new ArrayList<>();
        Set<Long> usedAreaIds = new HashSet<>();

        for (FilteredTourSpot startArea : candidates) {
            List<CourseComponent> course = buildCourse(startArea, candidates, userLat, userLon, formatter);

            resultCourses.add(course);
            course.forEach(a -> usedAreaIds.add(a.getId()));
            if (resultCourses.size() >= 5) break;
        }

        return resultCourses.stream()
                .map(course -> course.stream()
                        .map(CourseComponentResponse::from)
                        .toList())
                .toList();
    }

    private List<FilteredTourSpot> getFilteredCandidates(double userLat, double userLon, String startTime, String endTime, String tourspot, DateTimeFormatter formatter) {
        List<FilteredTourSpot> res =  tourSpotRepository.findAll().stream()
                .flatMap(area -> area.getFutureCongestions().stream()
                        .filter(f -> isInTimeRange(f.getFcstTime(), startTime, endTime))
                        .filter(f -> CongestionLevel.checkIsCourseComponent(f.getCongestionLvl().getCongestionLabel()))
                        .map(f -> new FilteredTourSpot(
                                area,
                                LocalDateTime.parse(f.getFcstTime(), formatter)
                        ))
                )
                .filter(a -> CourseUtil.calculateDistance(userLat, userLon, a.tourSpot().getAddress().getLatitude(), a.tourSpot().getAddress().getLongitude()) <= 30.0)
                .toList();



        return res;
    }

    private List<CourseComponent> buildCourse(FilteredTourSpot startArea, List<FilteredTourSpot> candidates,
                                               double userLat, double userLon, DateTimeFormatter formatter) {
        Set<TourSpot> visited = new HashSet<>();
        visited.add(startArea.tourSpot());

        List<CourseComponent> course = new ArrayList<>();
        FilteredTourSpot current = startArea;
        LocalDateTime currentTime = current.tourspotTm();
        course.add(courseMapper.toEntity(current));

        double dx = startArea.tourSpot().getAddress().getLatitude() - userLat;
        double dy = startArea.tourSpot().getAddress().getLongitude() - userLon;

        while (course.size() < 5) {
            FilteredTourSpot next = findNextAreaDirectional(current, candidates, visited, currentTime, dx, dy);

            if (next == null) break;
            visited.add(next.tourSpot());
            currentTime = next.tourspotTm();
            course.add(courseMapper.toEntity(next));
            current = next;
        }

        for (CourseComponent c : course){
            System.out.println("시작 시간: " + c.getTourspotTm());
        }
        return course;
    }

//    private boolean isValidCourse(List<Course> course, double userLat, double userLon, Set<Long> usedAreaIds) {
//        return course.size() >= 2
//                && CourseUtil.calculateDistance(userLat, userLon,
//                course.get(course.size() - 1).lat(), course.get(course.size() - 1).lon()) <= 30.0
//                && course.stream().noneMatch(a -> usedAreaIds.contains(a.name()));
//    }

    private boolean isInTimeRange(String time, String start, String end) {
        return time.compareTo(start) >= 0 && time.compareTo(end) <= 0;
    }

    private boolean isInDirection(FilteredTourSpot from, FilteredTourSpot to, double dx, double dy) {
        double vx = to.tourSpot().getAddress().getLongitude() - from.tourSpot().getAddress().getLongitude();
        double vy = to.tourSpot().getAddress().getLatitude() - from.tourSpot().getAddress().getLatitude(); // 이 부분 수정

        return (vx * dx + vy * dy) > 0;
    }

    private FilteredTourSpot findNextAreaDirectional(FilteredTourSpot from, List<FilteredTourSpot> candidates,
                                                     Set<TourSpot> visited, LocalDateTime currentTime,
                                                     double dx, double dy) {

        return candidates.stream()
                .peek(a -> {
                    if (visited.contains(a.tourSpot())) {
                        log.debug("방문된 장소 제외: {}", a.tourSpot().getTourspotNm());
                    } else if (!a.tourspotTm().equals(currentTime)) {
                        log.debug("a.tourSpotTm 시간 불일치: {}", a.tourspotTm());
                        log.debug("currentTime 시간 불일치: {}", currentTime);
                    } else {
                        double distance = CourseUtil.calculateDistance(
                                from.tourSpot().getAddress().getLatitude(), from.tourSpot().getAddress().getLongitude(),
                                a.tourSpot().getAddress().getLatitude(), a.tourSpot().getAddress().getLongitude()
                        );
                        if (distance > 30.0) {
                            log.debug("거리 초과: {} km, 장소: {}", distance, a.tourSpot().getTourspotNm());
                        } else if (!isInDirection(from, a, dx, dy)) {
                            log.debug("방향 벗어남: {}", a.tourSpot().getTourspotNm());
                        } else {
                            log.debug("후보 통과: {}", a.tourSpot().getTourspotNm());
                        }
                    }
                })
                .filter(a -> !visited.contains(a.tourSpot()))
                .filter(a -> a.tourspotTm().isEqual(currentTime.plusHours(1)))
                .filter(a -> CourseUtil.calculateDistance(
                        from.tourSpot().getAddress().getLatitude(), from.tourSpot().getAddress().getLongitude(),
                        a.tourSpot().getAddress().getLatitude(), a.tourSpot().getAddress().getLongitude()
                ) <= 30.0)
                .filter(a -> isInDirection(from, a, dx, dy))
                .min(Comparator.comparingDouble(a -> CourseUtil.calculateDistance(
                        from.tourSpot().getAddress().getLatitude(), from.tourSpot().getAddress().getLongitude(),
                        a.tourSpot().getAddress().getLatitude(), a.tourSpot().getAddress().getLongitude()
                )))
                .orElse(null);
    }

//    public Course likeCourse(CustomUserDetails customUserDetails, CourseLikeRequest request) {
//        User user = userRepository.findUserByEmail(customUserDetails.getEmail());
//        Course course = request.from(user.getId());
//        return courseRepository.save(course);
//    }
//
//    public CourseSpecResponse findCourseSpec(Long courseId){
//        return CourseSpecResponse.from(courseRepository.findById(courseId).orElseThrow());
//    }
}