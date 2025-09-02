package com.opendata.docs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import com.opendata.domain.tourspot.dto.response.TourSpotMetaResponse;
import com.opendata.domain.tourspot.dto.response.TourSpotRelatedResponse;
import com.opendata.global.response.ApiResponse;
import com.opendata.global.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "관광지 관련 API")
public interface TourSpotControllerDocs {

    @Operation(
            summary = "관광지 전체 데이터 수집",
            description = "모든 관광지 정보를 외부 API로부터 수집하여 저장. (프론트에서 호출하지 않음)"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "관광지 데이터 수집 및 저장 성공"),
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
    ResponseEntity<Void> getArea();


    @Operation(
            summary = "관광지 상세 정보 조회",
            description = "tourspotId로 관광지 상세 정보 조회"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "관광지 상세 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
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
    ResponseEntity<ApiResponse<TourSpotDetailResponse>> getTourSpotDetail(@PathVariable("tourspotId") Long tourspotId) throws JsonProcessingException;


    @Operation(
            summary = "관광지 메타 정보 조회",
            description = "페이징된 관광지 메타 정보(대표 이미지, 현재 혼잡도 등) 조회"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "관광지 메타 정보 조회 성공"),
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
    ResponseEntity<ApiResponse<PageResponse<List<TourSpotMetaResponse>>>> getTourSpotMeta(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) throws JsonProcessingException;


    @Operation(
            summary = "연관 관광지 조회",
            description = "특정 주소 ID(addressId)에 대한 연관 관광지 리스트 조회"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "연관 관광지 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "연관 관광지를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "연관 관광지 없음 응답",
                                    summary = "존재하지 않는 addressId",
                                    value = """
                                            {
                                              "success": false,
                                              "code": "TOURSPOT_RELATED_404",
                                              "message": "연관 관광지를 찾을 수 없습니다."
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
    ResponseEntity<ApiResponse<List<TourSpotRelatedResponse>>> getRelatedTourSpotDetail(@PathVariable("addressId") Long addressId);
}