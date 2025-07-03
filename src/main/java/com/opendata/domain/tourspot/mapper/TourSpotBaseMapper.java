package com.opendata.domain.tourspot.mapper;

import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotAssociated;
import com.opendata.global.mapper.BaseMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public interface TourSpotBaseMapper<D, E extends TourSpotAssociated> extends BaseMapper<D, E> {

    @AfterMapping
    default void afterToEntity(@MappingTarget E entity, TourSpot tourSpot){
        entity.assignTourSpot(tourSpot);
    }
}
