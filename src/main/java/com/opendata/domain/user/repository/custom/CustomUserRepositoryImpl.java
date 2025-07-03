//package com.opendata.domain.user.repository.custom;
//
//import com.opendata.domain.user.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@RequiredArgsConstructor
//public class CustomUserRepositoryImpl implements CustomUserRepository{
//    private final MongoTemplate mongoTemplate;
//    @Override
//    public User findUserByEmail(String email) {
//        Query query = new Query().addCriteria(Criteria.where("email").is(email));
//        return mongoTemplate.findOne(query, User.class);
//    }
//}
