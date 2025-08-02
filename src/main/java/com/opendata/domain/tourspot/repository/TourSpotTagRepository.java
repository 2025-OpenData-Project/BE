package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.TourSpotTag;
import com.opendata.domain.tourspot.repository.custom.CustomTourSpotTagRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourSpotTagRepository extends JpaRepository<TourSpotTag, Long>, CustomTourSpotTagRepository {
}
