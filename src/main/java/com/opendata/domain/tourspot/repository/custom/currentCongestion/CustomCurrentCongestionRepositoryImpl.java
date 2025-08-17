package com.opendata.domain.tourspot.repository.custom.currentCongestion;

import com.opendata.domain.tourspot.entity.QTourSpotCurrentCongestion;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotCurrentCongestion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomCurrentCongestionRepositoryImpl implements CustomCurrentCongestionRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public TourSpotCurrentCongestion findByTourSpotAndCurTime(TourSpot tourSpot, String fsctTime) {
        QTourSpotCurrentCongestion qTourSpotCurrentCongestion = QTourSpotCurrentCongestion.tourSpotCurrentCongestion;

        return queryFactory.selectFrom(qTourSpotCurrentCongestion)
                .where(qTourSpotCurrentCongestion.tourspot.eq(tourSpot), qTourSpotCurrentCongestion.fcstTime.eq(fsctTime))
                .fetchFirst();
    }
}
