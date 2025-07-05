package com.opendata.domain.tourspot.exception.message;

import com.opendata.global.response.BaseStatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CongestionErrorCode implements BaseStatusCode {

    INVALID_CONGESTION(HttpStatus.BAD_REQUEST,"CGST001", "유효하지 않은 혼잡도입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
