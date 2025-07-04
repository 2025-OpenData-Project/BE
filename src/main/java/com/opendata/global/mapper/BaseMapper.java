package com.opendata.global.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface BaseMapper<D, E> {
    D toDto(E entity);
    E toEntity(D dto);
}
