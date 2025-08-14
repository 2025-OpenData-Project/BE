package com.opendata.global.response.status;

import com.opendata.global.response.BaseStatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseStatusCode {
    // 일반 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON_403", "금지된 요청입니다."),

    // JWT
    EMPTY_JWT(HttpStatus.UNAUTHORIZED, "COMMON_404", "토큰이 비어있습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "COMMON_405", "유효하지 않은 토큰입니다."),

    // USER
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER_001", "이미 존재하는 사용자이며, 비밀번호가 틀렸습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_002", "해당 사용자를 찾을 수 없습니다."),

    //주소
    ADDRESS_NOT_FOUND(HttpStatus.BAD_REQUEST,"ADDRESS_001","해당 주소가 존재하지 않습니다."),

    //관광지
    TOURSPOT_NOT_FOUND(HttpStatus.BAD_REQUEST,"TOURSPOT_001","해당 관광지가 존재하지 않습니다."),
    TOURSPOT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"TOURSPOT_002","해당 관광지가 이미 존재합니다."),
    TOURSPOT_EXCEEDS(HttpStatus.BAD_REQUEST,"TOURSPOT_003","추가할 수 있는 관광지 개수가 초과되었습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
