package com.opendata.domain.course.dto.response;

import com.opendata.domain.apidata.dto.AreaComponentDto;

import java.util.List;

public record GeneratedCourse(
        List<AreaComponentDto> places
) {

    public static GeneratedCourse of(List<AreaComponentDto> path) {
        return new GeneratedCourse(path);
    }
}
