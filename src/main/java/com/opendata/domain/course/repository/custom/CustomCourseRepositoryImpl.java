package com.opendata.domain.course.repository.custom;

import com.opendata.domain.apidata.entity.Area;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class CustomCourseRepositoryImpl implements CustomCourseRepository{

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Area> findQuietAreas() {
        Query query = new Query(Criteria.where("congestion_level").in(1,2));

        return mongoTemplate.find(query, Area.class);
    }
}
