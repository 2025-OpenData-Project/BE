package com.opendata.domain.tourspot.repository.custom.total;

import com.opendata.domain.tourspot.dto.response.TourSpotMetaResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomTourSpotCombineRepository {
    List<TourSpotMetaResponse> findMetaPage(Pageable pageable);
    long countAll();
}
