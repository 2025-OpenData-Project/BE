package com.opendata.domain.apidata.repository;

import com.opendata.domain.apidata.entity.Area;
import com.opendata.domain.apidata.repository.custom.CustomAreaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AreaRepository extends MongoRepository<Area, String>, CustomAreaRepository {
}
