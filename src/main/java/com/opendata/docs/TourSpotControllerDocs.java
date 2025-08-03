package com.opendata.docs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "관광지 관련 API")
public interface TourSpotControllerDocs {

    @Operation(summary = "관광지 전체 데이터 수집", description = "모든 관광지 정보를 외부 API로부터 수집하여 저장, 프론트에서 호출하는 거 아님.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관광지 데이터 수집 및 저장 성공"),
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
    ResponseEntity<Void> getArea();


    @Operation(summary = "관광지 상세 정보 조회", description = "tourspotId로 관광지 상세 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관광지 상세 정보 조회 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 관광지를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "관광지 없음 응답",
                                    summary = "존재하지 않는 관광지 ID",
                                    value = """
                                    {
                                      "success": false,
                                      "code": "TOURSPOT_404",
                                      "message": "해당 관광지를 찾을 수 없습니다."
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
    ResponseEntity<com.opendata.global.response.ApiResponse<TourSpotDetailResponse>> getTourSpotDetail(@PathVariable("tourspotId") Long tourspotId) throws JsonProcessingException;
}