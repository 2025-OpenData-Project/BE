package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.QTourSpot;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomTourSpotRepositoryImpl implements CustomTourSpotRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<TourSpot> findByName(String name) {
        QTourSpot qTourSpot = QTourSpot.tourSpot;

        TourSpot result = queryFactory.selectFrom(qTourSpot)
                .where(qTourSpot.tourspotNm.eq(name))
                .fetchFirst();

        return Optional.ofNullable(result);
    }
}
