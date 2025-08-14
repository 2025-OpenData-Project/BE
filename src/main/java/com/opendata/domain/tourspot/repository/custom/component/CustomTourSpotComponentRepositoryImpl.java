package com.opendata.domain.tourspot.repository.custom.component;

import com.opendata.domain.tourspot.entity.QTourSpotComponent;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotComponent;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomTourSpotComponentRepositoryImpl implements CustomTourSpotComponentRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<TourSpotComponent> findAllByUserId(Long userId) {
        QTourSpotComponent q = QTourSpotComponent.tourSpotComponent;
        return queryFactory
                .selectFrom(q)
                .where(q.userId.eq(userId))
                .distinct()
                .fetch();
    }

    @Override
    public boolean existsByUserIdAndTourSpotId(Long userId, Long tourSpotId) {
        QTourSpotComponent q = QTourSpotComponent.tourSpotComponent;
        Integer fetchOne = queryFactory
                .selectOne()
                .from(q)
                .where(
                        q.userId.eq(userId)
                                .and(q.tourSpotId.eq(tourSpotId))
                )
                .fetchFirst(); // limit 1

        return fetchOne != null;
    }

    @Override
    public long countByUserId(Long userId) {
        QTourSpotComponent q = QTourSpotComponent.tourSpotComponent;
        return queryFactory
                .select(q.count())
                .from(q)
                .where(q.userId.eq(userId))
                .fetchOne();
    }

    @Override
    public void deleteByUserIdAndTourSpotId(Long userId, Long tourSpotId) {
        QTourSpotComponent q = QTourSpotComponent.tourSpotComponent;
        queryFactory
                .delete(q)
                .where(q.userId.eq(userId), q.tourSpotId.eq(tourSpotId))
                .execute();
    }
}
