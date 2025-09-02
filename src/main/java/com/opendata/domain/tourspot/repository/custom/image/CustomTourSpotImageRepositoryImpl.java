package com.opendata.domain.tourspot.repository.custom.image;

import com.opendata.domain.tourspot.entity.QTourSpotImage;
import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomTourSpotImageRepositoryImpl implements CustomTourSpotImageRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<TourSpotImage> findByTourSpot(TourSpot tourSpot) {
        QTourSpotImage qTourSpotImage = QTourSpotImage.tourSpotImage;
        return Optional.ofNullable(jpaQueryFactory.selectFrom(qTourSpotImage)
                .where(qTourSpotImage.tourspot.eq(tourSpot))
                .fetchFirst());
    }
}
