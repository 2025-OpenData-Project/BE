package com.opendata.domain.tourspot.repository.custom.futureCongestion;

import com.opendata.domain.tourspot.entity.QTourSpotFutureCongestion;
import com.opendata.domain.tourspot.entity.TourSpotFutureCongestion;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomFutureCongestionRepositoryImpl implements CustomFutureCongestionRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public long updateCongestionLevel(Long tourspotId, String fcstTime, CongestionLevel newLevel) {
        QTourSpotFutureCongestion qTourSpotFutureCongestion = QTourSpotFutureCongestion.tourSpotFutureCongestion;

        return queryFactory.update(qTourSpotFutureCongestion)
                .set(qTourSpotFutureCongestion.congestionLvl, newLevel)
                .where(qTourSpotFutureCongestion.tourspot.tourspotId.eq(tourspotId),
                        qTourSpotFutureCongestion.fcstTime.eq(fcstTime))
                .execute();
    }

    @Override
    public Optional<TourSpotFutureCongestion> findByTourSpotIdAndFcstTime(Long tourspotId, String fcstTime) {
        QTourSpotFutureCongestion qTourSpotFutureCongestion = QTourSpotFutureCongestion.tourSpotFutureCongestion;
        TourSpotFutureCongestion tourSpotFutureCongestion = queryFactory.selectFrom(qTourSpotFutureCongestion)
                .where(qTourSpotFutureCongestion.tourspot.tourspotId.eq(tourspotId),
                        qTourSpotFutureCongestion.fcstTime.eq(fcstTime))
                .fetchFirst();

        return Optional.ofNullable(tourSpotFutureCongestion);

    }
}
