package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.TourSpotEvent;
import com.opendata.domain.tourspot.repository.custom.event.CustomTourSpotEventRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TourSpotEventRepository extends JpaRepository<TourSpotEvent, Long>, CustomTourSpotEventRepository {

}
