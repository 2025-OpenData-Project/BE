package com.opendata.domain.tourspot.repository.custom;

import com.opendata.domain.tourspot.entity.TourSpotMonthlyCongestion;

import java.util.List;

public interface CustomMonthlyCongestionRepository
{
    long updateCongestionLevel(List<TourSpotMonthlyCongestion> monthlyCongestions);
}
