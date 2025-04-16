package com.opendata.domain.course.service;

import com.opendata.domain.apidata.dto.AreaComponentDto;
import com.opendata.domain.apidata.dto.FilteredArea;
import com.opendata.domain.apidata.entity.Area;
import com.opendata.domain.apidata.repository.AreaRepository;
import com.opendata.domain.course.dto.response.CourseLikeRequest;
import com.opendata.domain.course.dto.response.CourseResultResponse;
import com.opendata.domain.course.dto.response.CourseSpecResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.repository.CourseRepository;
import com.opendata.domain.course.util.CourseUtil;
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
    private final AreaRepository areaRepository;
    private final UserRepository userRepository;


    public CourseResultResponse recommendCourses(CustomUserDetails customUserDetails, double userLat, double userLon, String startTime, String endTime) {
        String email = customUserDetails.getEmail();
        User user = userRepository.findUserByEmail(email);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<FilteredArea> candidates = getFilteredCandidates(userLat, userLon, startTime, endTime, formatter);

        List<List<AreaComponentDto>> resultCourses = new ArrayList<>();
        Set<String> usedAreaIds = new HashSet<>();

        for (FilteredArea startArea : candidates) {
            List<AreaComponentDto> course = buildCourse(startArea, candidates, userLat, userLon, formatter);
            if (isValidCourse(course, userLat, userLon, usedAreaIds)) {
                resultCourses.add(course);
                course.forEach(a -> usedAreaIds.add(a.name()));
            }
            if (resultCourses.size() >= user.getMemberShip().getCourseLimit()) break;
        }

        return CourseResultResponse.from(resultCourses, resultCourses.size(), startTime, endTime);
    }

    private List<FilteredArea> getFilteredCandidates(double userLat, double userLon, String startTime, String endTime, DateTimeFormatter formatter) {
        return areaRepository.findAll().stream()
                .flatMap(area -> area.getFutures().stream()
                        .filter(f -> isInTimeRange(f.getFcstTime(), startTime, endTime))
                        .filter(f -> f.getFcstCongestLvl().equals("여유") || f.getFcstCongestLvl().equals("보통"))
                        .map(f -> new FilteredArea(
                                area.getId(),
                                area.getName(),
                                area.getCategory(),
                                area.getDescription(),
                                area.getImage(),
                                area.isIndoor(),
                                area.getLatitude(),
                                area.getLongitude(),
                                area.getCongestion_level(),
                                area.getEvents(),
                                LocalDateTime.parse(f.getFcstTime(), formatter)
                        ))
                )
                .filter(a -> CourseUtil.calculateDistance(userLat, userLon, a.lat(), a.lon()) <= 30.0)
                .toList();
    }

    private List<AreaComponentDto> buildCourse(FilteredArea startArea, List<FilteredArea> candidates,
                                               double userLat, double userLon, DateTimeFormatter formatter) {
        Set<String> visited = new HashSet<>();
        visited.add(startArea.areaId());

        List<AreaComponentDto> course = new ArrayList<>();
        FilteredArea current = startArea;
        LocalDateTime currentTime = current.time();
        course.add(AreaComponentDto.from(current, currentTime.format(formatter)));

        double dx = startArea.lat() - userLat;
        double dy = startArea.lon() - userLon;

        while (course.size() < 5) {
            FilteredArea next = findNextAreaDirectional(current, candidates, visited, currentTime, dx, dy);
            if (next == null) break;
            visited.add(next.areaId());
            currentTime = next.time();
            course.add(AreaComponentDto.from(next, currentTime.format(formatter)));
            current = next;
        }
        return course;
    }

    private boolean isValidCourse(List<AreaComponentDto> course, double userLat, double userLon, Set<String> usedAreaIds) {
        return course.size() >= 2
                && CourseUtil.calculateDistance(userLat, userLon,
                course.get(course.size() - 1).lat(), course.get(course.size() - 1).lon()) <= 30.0
                && course.stream().noneMatch(a -> usedAreaIds.contains(a.name()));
    }

    private boolean isInTimeRange(String time, String start, String end) {
        return time.compareTo(start) >= 0 && time.compareTo(end) <= 0;
    }

    private boolean isInDirection(FilteredArea from, FilteredArea to, double dx, double dy) {
        double vx = to.lon() - from.lon();
        double vy = to.lat() - from.lat();
        return (vx * dy + vy * dx) > 0;
    }

    private FilteredArea findNextAreaDirectional(FilteredArea from, List<FilteredArea> candidates,
                                                 Set<String> visited, LocalDateTime currentTime,
                                                 double dx, double dy) {
        return candidates.stream()
                .filter(a -> !visited.contains(a.areaId()))
                .filter(a -> a.time().equals(currentTime.plusHours(1)))
                .filter(a -> CourseUtil.calculateDistance(from.lat(), from.lon(), a.lat(), a.lon()) <= 30.0)
                .filter(a -> isInDirection(from, a, dx, dy))
                .min(Comparator.comparingDouble(a -> CourseUtil.calculateDistance(from.lat(), from.lon(), a.lat(), a.lon())))
                .orElse(null);
    }

    public Course likeCourse(CustomUserDetails customUserDetails, CourseLikeRequest request) {
        User user = userRepository.findUserByEmail(customUserDetails.getEmail());
        Course course = request.from(user.getId());
        return courseRepository.save(course);
    }

    public CourseSpecResponse findCourseSpec(String courseId){
        return CourseSpecResponse.from(courseRepository.findById(courseId).orElseThrow());
    }
}