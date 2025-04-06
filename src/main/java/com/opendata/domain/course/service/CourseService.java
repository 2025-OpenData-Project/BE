package com.opendata.domain.course.service;

import com.opendata.domain.apidata.dto.AreaComponentDto;
import com.opendata.domain.apidata.entity.Area;
import com.opendata.domain.course.dto.response.CourseResultResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.domain.course.repository.CourseRepository;
import com.opendata.domain.course.util.CourseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
//
//    public List<CourseResultResponse> findCoursesByCurrentPosition(String lat, String lon){
//        List<Area> areas = courseRepository.findQuietAreas();
//
//    }
    public List<CourseResultResponse> findAllCourses(double userLat, double userLon, String startTime, String endTime) {
        List<List<AreaComponentDto>> allCourses = new ArrayList<>();

        List<Area> candidates = courseRepository.findQuietAreas();
        for (Area start : candidates) {
            double distance = CourseUtil.calculateDistance(userLat, userLon, start.getLatitude(), start.getLongitude());
            if (distance <= 30.0) {
                List<AreaComponentDto> currentPath = new ArrayList<>();
                Set<String> visited = new HashSet<>();
                currentPath.add(AreaComponentDto.from(start));
                visited.add(start.getId());
                dfs(start, candidates, visited, currentPath, allCourses);
            }
        }

        return List.of(CourseResultResponse.from(allCourses, allCourses.size(), startTime, endTime));
    }

    private void dfs(Area current, List<Area> candidates, Set<String> visited,
                     List<AreaComponentDto> currentPath, List<List<AreaComponentDto>> allCourses) {

        // 현재 경로 저장 (복사해서 저장)
        allCourses.add(new ArrayList<>(currentPath));

        for (Area next : candidates) {
            if (visited.contains(next.getId())) continue;

            double distance = CourseUtil.calculateDistance(current.getLatitude(), current.getLongitude(),
                    next.getLatitude(), next.getLongitude());
            if (distance <= 30.0) {
                visited.add(next.getId());
                currentPath.add(AreaComponentDto.from(next));
                dfs(next, candidates, visited, currentPath, allCourses);
                currentPath.remove(currentPath.size() - 1);
                visited.remove(next.getId());
            }
        }
    }
}
