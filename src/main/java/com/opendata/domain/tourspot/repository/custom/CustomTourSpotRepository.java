package com.opendata.domain.tourspot.repository.custom;

import com.opendata.domain.tourspot.entity.TourSpot;

import java.util.Optional;

public interface CustomTourSpotRepository {
    Optional<TourSpot> findByName(String name);
}
