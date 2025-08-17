package com.opendata.domain.tourspot.repository.custom.tag;

import com.opendata.domain.tourspot.entity.QTourSpotTag;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomTourSpotTagRepositoryImpl implements CustomTourSpotTagRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<TourSpotTag> findAllByTourSpot(TourSpot tourSpot) {
        QTourSpotTag qTourSpotTag = QTourSpotTag.tourSpotTag;
        return queryFactory.selectFrom(qTourSpotTag)
                .where(qTourSpotTag.tourspot.eq(tourSpot))
                .fetch();
    }
}
