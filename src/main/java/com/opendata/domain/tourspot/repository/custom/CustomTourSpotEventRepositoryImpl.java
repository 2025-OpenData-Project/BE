package com.opendata.domain.tourspot.repository.custom;

import com.opendata.domain.tourspot.entity.QTourSpotEvent;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotEvent;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public List<TourSpotEvent> findAllByTourSpot(TourSpot tourSpot) {
        QTourSpotEvent qTourSpotEvent = QTourSpotEvent.tourSpotEvent;
        return queryFactory.selectFrom(qTourSpotEvent)
                .where(qTourSpotEvent.tourspot.eq(tourSpot))
                .fetch();
    }
}
