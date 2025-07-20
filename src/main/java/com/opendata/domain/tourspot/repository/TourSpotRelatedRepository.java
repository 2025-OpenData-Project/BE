package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.TourSpotRelated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourSpotRelatedRepository extends JpaRepository<TourSpotRelated, Long>,CustomTourSpotRelatedRepository {
}
