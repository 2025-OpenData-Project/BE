package com.opendata.domain.tourspot.repository.custom.tag;

import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotTag;

import java.util.List;

public interface CustomTourSpotTagRepository {
    List<TourSpotTag> findAllByTourSpot(TourSpot tourSpot);
}
