package com.opendata.domain.apidata.repository.custom;

import com.opendata.domain.apidata.entity.Area;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomAreaRepository
{
    public void upsertAreaList(List<Area>areas);
    List<Area> findAreaWithCongestionByCurrentTime(String time);
}
