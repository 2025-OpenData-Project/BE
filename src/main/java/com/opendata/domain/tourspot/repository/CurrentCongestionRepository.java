package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.TourSpotCurrentCongestion;
import com.opendata.domain.tourspot.repository.custom.currentCongestion.CustomCurrentCongestionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentCongestionRepository extends JpaRepository<TourSpotCurrentCongestion, Long>, CustomCurrentCongestionRepository {
}
