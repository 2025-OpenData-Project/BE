package com.opendata.domain.tourspot.repository.custom.component;

import com.opendata.domain.tourspot.entity.TourSpotComponent;

import java.util.List;

public interface CustomTourSpotComponentRepository {
    List<TourSpotComponent> findAllByUserId(Long userId);
    boolean existsByUserIdAndTourSpotId(Long userId, Long tourSpotId);

    long countByUserId(Long userId);

    void deleteByUserIdAndTourSpotId(Long userId, Long tourSpotId);
}
