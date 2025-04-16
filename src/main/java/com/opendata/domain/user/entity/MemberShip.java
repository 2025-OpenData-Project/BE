package com.opendata.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberShip {
    FREE(3), PREMIUM(10);

    private final int courseLimit;

}
