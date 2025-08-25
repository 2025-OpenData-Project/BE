package com.opendata.domain.tourspot.repository.custom.related;

import com.opendata.domain.address.entity.Address;
import com.opendata.domain.tourspot.dto.response.TourSpotRelatedResponse;
import com.opendata.domain.tourspot.entity.QTourSpotRelated;
import com.opendata.domain.tourspot.entity.TourSpotRelated;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomTourSpotRelatedRepositoryImpl implements CustomTourSpotRelatedRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Optional<List<TourSpotRelatedResponse>> findAllByAddress(Address address) {
        QTourSpotRelated qTourSpotRelated = QTourSpotRelated.tourSpotRelated;

        return Optional.ofNullable(jpaQueryFactory.select(Projections.constructor(TourSpotRelatedResponse.class,
                        qTourSpotRelated.id, qTourSpotRelated.tourSpotCode, qTourSpotRelated.tourSpotName,
                        qTourSpotRelated.largeCategory, qTourSpotRelated.middleCategory,
                        qTourSpotRelated.mapX, qTourSpotRelated.mapY))
                        .from(qTourSpotRelated)
                .where(qTourSpotRelated.address.eq(address))
                .fetch());
    }
}
