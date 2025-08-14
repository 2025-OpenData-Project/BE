package com.opendata.domain.tourspot.repository.custom.component;

import com.opendata.domain.tourspot.entity.TourSpotComponent;

import java.util.List;

public interface CustomTourSpotComponentRepository {
    List<TourSpotComponent> findAllByUserId(Long userId);
}
