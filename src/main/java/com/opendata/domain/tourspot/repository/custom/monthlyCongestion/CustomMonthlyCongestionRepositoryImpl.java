package com.opendata.domain.tourspot.repository.custom.monthlyCongestion;

import com.opendata.domain.tourspot.entity.QTourSpot;
import com.opendata.domain.tourspot.entity.QTourSpotMonthlyCongestion;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotMonthlyCongestion;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomMonthlyCongestionRepositoryImpl implements CustomMonthlyCongestionRepository
{

    private final JPAQueryFactory queryFactory;
    @Override
    public long updateCongestionLevel(List<TourSpotMonthlyCongestion> monthlyCongestions) {
        return 1L;
    }

    @Override
    @Transactional
    public void deleteMonthlyCongestionsByTourspotId(Long tourspotId) {
        QTourSpotMonthlyCongestion mc = QTourSpotMonthlyCongestion.tourSpotMonthlyCongestion;

        queryFactory
            .delete(mc)
            .where(mc.tourspot.tourspotId.eq(tourspotId))
            .execute();
    }

    @Override
    public List<TourSpotMonthlyCongestion> findAllByTourspot(TourSpot tourSpot) {
        QTourSpotMonthlyCongestion mc = QTourSpotMonthlyCongestion.tourSpotMonthlyCongestion;

        return queryFactory
            .selectFrom(mc)
            .where(mc.tourspot.tourspotId.eq(tourSpot.getTourspotId()))
            .orderBy(mc.baseYmd.asc())
            .fetch();
    }

}
