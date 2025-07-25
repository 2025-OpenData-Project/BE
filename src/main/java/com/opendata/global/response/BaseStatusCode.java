package com.opendata.global.response;

import org.springframework.http.HttpStatus;

public interface BaseStatusCode {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
