package com.opendata.domain.user.repository;

import com.opendata.domain.user.entity.User;
import com.opendata.domain.user.repository.custom.CustomUserRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository {
}
