package com.opendata.domain.tourspot.repository.custom.monthlyCongestion;

import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotMonthlyCongestion;

import java.util.List;

public interface CustomMonthlyCongestionRepository
{
    long updateCongestionLevel(List<TourSpotMonthlyCongestion> monthlyCongestions);
    void deleteMonthlyCongestionsByTourspotId(Long tourspotId);
    List<TourSpotMonthlyCongestion> findAllByTourspot(TourSpot tourSpot);
}
