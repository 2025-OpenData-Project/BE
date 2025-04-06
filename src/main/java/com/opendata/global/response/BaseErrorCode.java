package com.opendata.global.response;

public interface BaseErrorCode {
    ErrorInfoDto getReasonHttpStatus();
    String getMessage();
}
