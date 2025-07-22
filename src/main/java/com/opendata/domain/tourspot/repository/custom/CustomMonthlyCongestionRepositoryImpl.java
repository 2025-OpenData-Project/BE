package com.opendata.domain.tourspot.repository.custom;

import com.opendata.domain.tourspot.entity.TourSpotMonthlyCongestion;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
}
