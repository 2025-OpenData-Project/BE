package com.opendata.docs;

import com.opendata.domain.course.dto.response.CourseComponentResponse;
import com.opendata.domain.course.dto.response.CourseLikeRequest;
import com.opendata.domain.course.dto.response.CourseResultResponse;
import com.opendata.domain.course.dto.response.CourseSpecResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.global.response.ApiResponse;
import com.opendata.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "코스 추천/관리 API")
public interface CourseControllerDocs {

    @Operation(summary = "추천 관광 코스 조회", description = "현재 위치(lat, lon)와 시간(startTime, endTime), 관광지명을 기반으로 관광 코스를 추천합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "관광 코스 추천 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 파라미터",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "잘못된 요청",
                                    summary = "파라미터 누락 또는 형식 오류",
                                    value = """
                                    {
                                      "success": false,
                                      "code": "COMMON_400",
                                      "message": "요청 파라미터가 올바르지 않습니다."
                                    }
                                    """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                                    {
                                      "success": false,
                                      "code": "COMMON_500",
                                      "message": "서버 에러, 관리자에게 문의 바랍니다."
                                    }
                                    """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponse<List<List<CourseComponentResponse>>>> findCourses(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam String tourspot
    );

//    @Operation(summary = "관광 코스 좋아요 등록", description = "사용자가 특정 관광 코스를 좋아요할 수 있습니다.")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "좋아요 등록 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(
//                    responseCode = "401",
//                    description = "인증 실패",
//                    content = @Content(
//                            mediaType = "application/json",
//                            examples = @ExampleObject(
//                                    name = "인증 실패",
//                                    summary = "로그인 필요",
//                                    value = """
//                                    {
//                                      "success": false,
//                                      "code": "COMMON_401",
//                                      "message": "인증이 필요합니다."
//                                    }
//                                    """
//                            )
//                    )
//            )
//    })
//    ResponseEntity<ApiResponse<Course>> postCourseLike(
//            CustomUserDetails customUserDetails,
//            CourseLikeRequest request
//    );
//
//    @Operation(summary = "관광 코스 상세 조회", description = "관광 코스 ID로 세부 스펙(시간대별 장소 등)을 조회합니다.")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "코스 상세 정보 조회 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(
//                    responseCode = "404",
//                    description = "코스 정보 없음",
//                    content = @Content(
//                            mediaType = "application/json",
//                            examples = @ExampleObject(
//                                    name = "코스 없음",
//                                    summary = "존재하지 않는 코스 ID",
//                                    value = """
//                                    {
//                                      "success": false,
//                                      "code": "COURSE_404",
//                                      "message": "해당 코스를 찾을 수 없습니다."
//                                    }
//                                    """
//                            )
//                    )
//            )
//    })
//    ResponseEntity<ApiResponse<CourseSpecResponse>> getCourseSpec(@PathVariable Long courseId);
}