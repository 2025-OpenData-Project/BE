package com.opendata.domain.user.service;

import com.opendata.domain.user.entity.User;
import com.opendata.domain.user.exception.UserNotFoundException;
import com.opendata.domain.user.message.UserMessages;
import com.opendata.domain.user.repository.UserRepository;
import com.opendata.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;


    public User findUserByEmailOrThrow(CustomUserDetails customUserDetails){
        return Optional.ofNullable(userRepository.findUserByEmail(customUserDetails.getEmail()))
                .orElseThrow(() -> new UserNotFoundException(UserMessages.USER_NOT_FOUND));
    }

}
