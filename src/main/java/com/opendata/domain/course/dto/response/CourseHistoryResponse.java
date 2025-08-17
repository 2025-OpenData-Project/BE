package com.opendata.domain.course.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record CourseHistoryResponse(
        String courseId,
        LocalDateTime startDtm,
        LocalDateTime endDtm,
        List<CourseComponentHistoryDto> history
) {
}
