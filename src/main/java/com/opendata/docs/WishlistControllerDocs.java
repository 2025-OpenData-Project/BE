package com.opendata.docs;

import com.opendata.domain.course.dto.response.CourseSpecResponse;
import com.opendata.domain.course.entity.Course;
import com.opendata.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@Tag(name = "WishList 서비스 API")
public interface WishlistControllerDocs
{
    @Operation(summary = "코스 조회", description = "CourseId를 이용해 원하는 코스 하나 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
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
    ResponseEntity<com.opendata.global.response.ApiResponse<Course>> findCourse(@RequestParam String courseId);



    @Operation(summary = "사용자 별 코스 리스트 조회", description = "사용자가 저장한 코스 List 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 조회 성공"),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 실패 응답",
                                    summary = "유효하지 않은 토큰 또는 로그인 필요",
                                    value = """
            {
              "success": false,
              "code": "COMMON_401",
              "message": "인증이 필요합니다."
            }
            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 에러",
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
    ResponseEntity<com.opendata.global.response.ApiResponse<List<Course>>> findCourses(@AuthenticationPrincipal CustomUserDetails userDetails);




    @Operation(summary = "코스 삭제", description = "위시리스트에서 코스 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스삭제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
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
    ResponseEntity<com.opendata.global.response.ApiResponse<String>> deleteCourse(@RequestParam String courseId);



    @Operation(summary = "코스 선택", description = "코스 활성화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 조회 성공"),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 실패 응답",
                                    summary = "유효하지 않은 토큰 또는 로그인 필요",
                                    value = """
            {
              "success": false,
              "code": "COMMON_401",
              "message": "인증이 필요합니다."
            }
            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 에러",
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
    ResponseEntity<com.opendata.global.response.ApiResponse<Course>> selectCourse(@RequestParam String courseId, @AuthenticationPrincipal CustomUserDetails userDetails);



    @Operation(summary = "코스 공유", description = "코스 공유 url 제공")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "url 제공 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
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
    ResponseEntity<com.opendata.global.response.ApiResponse<CourseSpecResponse>> getSharedCourse(@PathVariable String courseId);
}
