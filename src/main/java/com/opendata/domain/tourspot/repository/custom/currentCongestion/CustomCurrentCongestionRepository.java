package com.opendata.domain.tourspot.repository.custom.currentCongestion;

import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotCurrentCongestion;

public interface CustomCurrentCongestionRepository {
    TourSpotCurrentCongestion findByTourSpotAndCurTime(TourSpot tourSpot, String fsctTime);
}
