package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.TourSpotFutureCongestion;
import com.opendata.domain.tourspot.repository.custom.CustomFutureCongestionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FutureCongestionRepository extends JpaRepository<TourSpotFutureCongestion, Long>, CustomFutureCongestionRepository {
}
