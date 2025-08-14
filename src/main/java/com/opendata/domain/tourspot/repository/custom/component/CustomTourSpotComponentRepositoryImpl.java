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
}
