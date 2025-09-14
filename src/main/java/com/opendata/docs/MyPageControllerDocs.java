package com.opendata.docs;


import com.opendata.domain.course.dto.response.CourseHistoryResponse;
import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import com.opendata.domain.user.dto.UserResponse;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "마이페이지 API")
public interface MyPageControllerDocs {
    @Operation(summary = "과거 코스 조회", description = "현재 코스 포함 사용자 코스 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 코스 조회 성공"),
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
            @ApiResponse(
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
    ResponseEntity<com.opendata.global.response.ApiResponse<List<CourseHistoryResponse>>> findCourses(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    );

    @Operation(summary = "사용자 선호 관광지 추가", description = "사용자가 선호하는 관광지 5개만 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 선호 관광지 추가 성공"),
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
            @ApiResponse(
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
    ResponseEntity<com.opendata.global.response.ApiResponse<Void>> updateTourSpot(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("tourSpotId") Long tourSpotId
    );

    @Operation(summary = "사용자 선호 관광지 조회", description = "사용자가 선호하는 관광지 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 선호 관광지 조회 성공"),
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
            @ApiResponse(
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
    ResponseEntity<com.opendata.global.response.ApiResponse<List<TourSpotDetailResponse>>> findTourSpot(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    );

    @Operation(summary = "사용자 선호 관광지 삭제", description = "사용자가 선호 관광지 삭제 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 선호 관광지 삭제 성공"),
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
            @ApiResponse(
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
    ResponseEntity<com.opendata.global.response.ApiResponse<Void>> deleteTourSpot(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("tourSpotId") Long tourSpotId
    );

    @Operation(summary = "사용자 조회", description = "사용자 정보(이메일, 멤버쉽, 이름) 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
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
            @ApiResponse(
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
    ResponseEntity<com.opendata.global.response.ApiResponse<UserResponse>> findUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    );



    @Operation(summary = "관광지 선호 가능 여부 조회", description = "이미 선호관광지에 추가했으면 true 안 했으면 false 리턴")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "선호 가능 여부 조회 성공"),
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
        @ApiResponse(
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
    ResponseEntity<com.opendata.global.response.ApiResponse<Boolean>> CheckTourSpot(
        @PathVariable("tourSpotId") Long tourSpotId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    );
}
