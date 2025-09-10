package com.opendata.domain.tourspot.repository.custom.total;

import com.opendata.domain.tourspot.dto.response.TourSpotMetaResponse;
import com.opendata.domain.tourspot.entity.QTourSpot;
import com.opendata.domain.tourspot.entity.QTourSpotCurrentCongestion;
import com.opendata.domain.tourspot.entity.QTourSpotImage;
import com.opendata.global.util.DateUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomTourSpotCombineRepositoryImpl implements CustomTourSpotCombineRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<TourSpotMetaResponse> findMetaByRank() {
        QTourSpot s = QTourSpot.tourSpot;
        QTourSpotImage img = QTourSpotImage.tourSpotImage;
        QTourSpotCurrentCongestion cc = QTourSpotCurrentCongestion.tourSpotCurrentCongestion;

        return queryFactory
            .select(Projections.constructor(TourSpotMetaResponse.class,
                s.tourspotId,
                s.tourspotNm,
                img.tourspotImgUrl.min(),
                cc.congestionLvl.max()
            ))
            .from(s)
            .leftJoin(img).on(img.tourspot.eq(s))
            .leftJoin(cc).on(cc.tourspot.eq(s)
                .and(cc.fcstTime.eq(DateUtil.getCurrentRoundedFormattedDateTime())))
            .groupBy(s.tourspotId, s.tourspotNm, s.viewCount)
            .orderBy(s.viewCount.desc())
            .offset(0)
            .limit(10)
            .fetch();
    }

    @Override
    public List<TourSpotMetaResponse> findMetaPage(Pageable pageable) {
        QTourSpot s = QTourSpot.tourSpot;
        QTourSpotImage img = QTourSpotImage.tourSpotImage;
        QTourSpotCurrentCongestion cc = QTourSpotCurrentCongestion.tourSpotCurrentCongestion;

        return queryFactory
            .select(Projections.constructor(TourSpotMetaResponse.class,
                s.tourspotId,
                s.tourspotNm,
                img.tourspotImgUrl.min(),
                cc.congestionLvl.min()
            ))
            .from(s)
            .leftJoin(img).on(img.tourspot.eq(s))
            .leftJoin(cc).on(cc.tourspot.eq(s)
                .and(cc.fcstTime.eq(DateUtil.getCurrentRoundedFormattedDateTime())))
            .orderBy(s.tourspotId.asc())
            .groupBy(s.tourspotId, s.tourspotNm)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public long countAll() {
        QTourSpot s = QTourSpot.tourSpot;

        Long count = queryFactory
            .select(s.count())
            .from(s)
            .fetchOne();

        return count == null ? 0 : count;
    }
}