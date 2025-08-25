package com.opendata.domain.tourspot.repository.custom.related;

import com.opendata.domain.address.entity.Address;
import com.opendata.domain.tourspot.dto.response.TourSpotRelatedResponse;


import java.util.List;
import java.util.Optional;

public interface CustomTourSpotRelatedRepository {
    Optional<List<TourSpotRelatedResponse>> findAllByAddress(Address address);
}
