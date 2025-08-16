package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.repository.custom.tourSpot.CustomTourSpotRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourSpotRepository extends JpaRepository<TourSpot, Long>, CustomTourSpotRepository {
}
