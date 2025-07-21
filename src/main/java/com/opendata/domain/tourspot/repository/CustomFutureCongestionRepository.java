package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.TourSpotFutureCongestion;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;

import java.util.Optional;


public interface CustomFutureCongestionRepository {
    long updateCongestionLevel(Long tourspotId, String fcstTime, CongestionLevel newLevel);
    Optional<TourSpotFutureCongestion> findByTourSpotIdAndFcstTime(Long tourspotId, String fcstTime);
}
