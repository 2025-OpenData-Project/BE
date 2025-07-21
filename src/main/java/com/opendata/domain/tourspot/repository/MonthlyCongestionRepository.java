package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.TourSpotFutureCongestion;
import com.opendata.domain.tourspot.entity.TourSpotMonthlyCongestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyCongestionRepository extends JpaRepository<TourSpotMonthlyCongestion, Long>,CustomMonthlyCongestionRepository {
}
