package com.opendata.domain.tourspot.entity.enums;

import com.opendata.domain.tourspot.exception.CongestionException;
import com.opendata.domain.tourspot.exception.message.CongestionErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum CongestionLevel {
    RELAXED("여유"), NORMAL("보통"), SLIGHTLY_BUSY("약간 붐빔"), BUSY("붐빔");

    private final String congestionLabel;

    public static final Map<String, CongestionLevel> CACHE =
            Arrays.stream(values()).collect(Collectors.toMap(CongestionLevel::getCongestionLabel, Function.identity()));


    public static CongestionLevel resolve(String congestion){
        CongestionLevel level = CACHE.get(congestion);
        if (level == null){
            throw new CongestionException(CongestionErrorCode.INVALID_CONGESTION);
        }
        return level;
    }

    private boolean isCourseComponent(){
        return this == RELAXED || this == NORMAL;
    }

    public static boolean checkIsCourseComponent(String congestion){
        return resolve(congestion).isCourseComponent();
    }
}
