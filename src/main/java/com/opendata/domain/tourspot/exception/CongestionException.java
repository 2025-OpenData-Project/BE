package com.opendata.domain.tourspot.exception;

import com.opendata.global.response.BaseStatusCode;
import com.opendata.global.response.exception.GlobalException;

public class CongestionException extends GlobalException {
    public CongestionException(BaseStatusCode code) {
        super(code);
    }
}
