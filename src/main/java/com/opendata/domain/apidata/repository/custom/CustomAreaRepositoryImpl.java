package com.opendata.domain.apidata.repository.custom;

import com.opendata.domain.apidata.entity.Area;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomAreaRepositoryImpl implements CustomAreaRepository
{
    private final MongoTemplate mongoTemplate;
    @Override
    public void upsertAreaList(List<Area> areas)
    {
        for(Area area : areas)
        {
            Query query = new Query();
            query.addCriteria(Criteria.where("name").is(area.getName()));
            Update update = new Update()
                    .set("name", area.getName())
                    .set("congestion_level", area.getCongestion_level())
                    .set("events", area.getEvents())
                    .set("futures",area.getFutures());
            mongoTemplate.upsert(query, update, Area.class);
        }
    }
}
