package com.opendata.domain.register.dto;

import com.opendata.domain.user.entity.MemberShip;
import com.opendata.domain.user.entity.User;


public record RegisterRequest(
        String email,
        String password
) {
    public User from(String encodedPassword){
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .memberShip(MemberShip.FREE)
                .build();
    }
}

