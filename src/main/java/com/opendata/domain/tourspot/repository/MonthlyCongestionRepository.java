package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.TourSpotMonthlyCongestion;
import com.opendata.domain.tourspot.repository.custom.montlyCongestion.CustomMonthlyCongestionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyCongestionRepository extends JpaRepository<TourSpotMonthlyCongestion, Long>, CustomMonthlyCongestionRepository {
}
