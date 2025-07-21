package com.opendata.domain.tourspot.repository.custom;

public interface CustomTourSpotEventRepository {
    boolean existsByEventNameAndEventPeriod(String eventName, String eventPeriod);
}
