package com.opendata.domain.tourspot.repository;

import com.opendata.domain.tourspot.entity.TourSpotImage;
import com.opendata.domain.tourspot.repository.custom.image.CustomTourSpotImageRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourSpotImageRepository extends JpaRepository<TourSpotImage, Long>, CustomTourSpotImageRepository {
}
