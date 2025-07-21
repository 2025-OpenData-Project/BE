package com.opendata.domain.tourspot.repository.custom;

import com.opendata.domain.tourspot.entity.QTourSpotEvent;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomTourSpotEventRepositoryImpl implements CustomTourSpotEventRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByEventNameAndEventPeriod(String eventName, String eventPeriod) {
        QTourSpotEvent qTourSpotEvent = QTourSpotEvent.tourSpotEvent;

        Integer fetchOne = queryFactory
                .selectOne()
                .from(qTourSpotEvent)
                .where(
                        qTourSpotEvent.eventName.eq(eventName),
                        qTourSpotEvent.eventPeriod.eq(eventPeriod)
                )
                .fetchFirst();
        return fetchOne != null;
    }
}
