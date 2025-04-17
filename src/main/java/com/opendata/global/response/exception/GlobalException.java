package com.opendata.global.response.exception;

import com.opendata.global.response.BaseErrorCode;
import com.opendata.global.response.ErrorInfoDto;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final BaseErrorCode code;

    public GlobalException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ErrorInfoDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }

}