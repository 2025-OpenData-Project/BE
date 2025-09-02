package com.opendata.domain.tourspot.repository.custom.image;

import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotImage;

import java.util.Optional;

public interface CustomTourSpotImageRepository {
    Optional<TourSpotImage> findByTourSpot(TourSpot tourSpot);
}
