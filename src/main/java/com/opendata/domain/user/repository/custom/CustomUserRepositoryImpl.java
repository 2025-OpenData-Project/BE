package com.opendata.domain.user.repository.custom;

import com.opendata.domain.user.entity.QUser;
import com.opendata.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public User findUserByEmail(String email) {
        QUser u = QUser.user;
        return queryFactory
                .selectFrom(u)
                .where(u.email.eq(email))
                .fetchFirst();
    }

    @Override
    public User findUserById(Long userId) {
        QUser u = QUser.user;
        return queryFactory
                .selectFrom(u)
                .where(u.id.eq(userId))
                .fetchFirst();
    }

    @Override
    public void deleteUserById(Long userId) {
        QUser u = QUser.user;
        queryFactory
                .delete(u)
                .where(u.id.eq(userId))
                .execute();
    }
}
