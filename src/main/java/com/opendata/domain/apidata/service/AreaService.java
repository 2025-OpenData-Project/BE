package com.opendata.domain.apidata.service;


import com.opendata.domain.apidata.api.AreaApi;
import com.opendata.domain.apidata.dto.CityDataDto;
import com.opendata.domain.apidata.entity.Area;
import com.opendata.domain.apidata.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AreaService
{
    private final CityDataService cityDataService;
    private final AreaRepository areaRepository;

    public void fetchAllAreaAndSave()
    {
        List<String> areaNames = new AreaApi.AreaParam().getAreaInfos();
        List<CompletableFuture<CityDataDto>> futures = areaNames.stream()
                .map(cityDataService::fetchCityData)
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();//끝날때까지 기다리기

        List<Area> areaList = futures.stream()
                .map(CompletableFuture::join)
                .map(this::convertToEntity)
                .filter(Objects::nonNull)
                .toList();
        areaRepository.saveAll(areaList);

    }

    public Area convertToEntity(CityDataDto cityDataDto)
    {
        if(cityDataDto.getCitydata() == null)
        {
            return null;
        }
        var data = cityDataDto.getCitydata();
        Area area = new Area();
        area.setName(data.getAreaName());
        area.setEvents(data.getEventDataList());
        if (data.getAreaCongestLvl()== null)
        {
            area.setCongestion_level(0);
        }
        else if(data.getAreaCongestLvl().equals("여유"))
        {
            area.setCongestion_level(1);
        }
        else if(data.getAreaCongestLvl().equals("보통"))
        {
            area.setCongestion_level(2);
        }
        else if(data.getAreaCongestLvl().equals("약간 붐빔"))
        {
            area.setCongestion_level(3);
        }
        else {
            area.setCongestion_level(4);
        }
        return area;
    }
}
