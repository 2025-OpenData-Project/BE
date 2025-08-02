package com.opendata.domain.tourspot.repository.custom;

import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotEvent;

import java.util.List;

public interface CustomTourSpotEventRepository {
    boolean existsByEventNameAndEventPeriod(String eventName, String eventPeriod);
    List<TourSpotEvent> findAllByTourSpot(TourSpot tourSpot);
}
