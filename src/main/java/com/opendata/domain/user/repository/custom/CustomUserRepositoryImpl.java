package com.opendata.domain.user.repository.custom;

import com.opendata.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository{

    @Override
    public User findUserByEmail(String email) {
        return null;
    }
}
