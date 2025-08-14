package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.TourSpotComponent;
import com.opendata.domain.tourspot.repository.custom.component.CustomTourSpotComponentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourSpotComponentRepository extends JpaRepository<TourSpotComponent,Long> , CustomTourSpotComponentRepository {
}
