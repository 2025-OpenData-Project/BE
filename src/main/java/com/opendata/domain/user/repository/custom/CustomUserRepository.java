package com.opendata.domain.user.repository.custom;


import com.opendata.domain.user.entity.User;

public interface CustomUserRepository {
    User findUserByEmail(String email);
}
