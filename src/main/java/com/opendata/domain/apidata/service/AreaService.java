package com.opendata.domain.apidata.service;


import com.opendata.domain.apidata.api.AreaApi;
import com.opendata.domain.apidata.dto.CityDataDto;
import com.opendata.domain.apidata.entity.Area;
import com.opendata.domain.apidata.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AreaService
{
    private final CityDataService cityDataService;
    private final AreaRepository areaRepository;

    //@Async
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
        areaRepository.upsertAreaList(areaList);

    }

    public Area convertToEntity(CityDataDto cityDataDto)
    {
        if(cityDataDto.getCitydata() == null)
        {
            return null;
        }
        var data = cityDataDto.getCitydata();

        List<CityDataDto.LivePopulationStatus> livePopulationStatuses = data.getLivePopulationStatuses();


        Area area = new Area();
        area.setName(data.getAreaName());
        area.setEvents(data.getEventDataList());
        area.setFutures(livePopulationStatuses.get(0).getFCstPpltn());

        String congestLevel= livePopulationStatuses.get(0).getAreaCongestLvl();

        if (congestLevel== null)
        {
            area.setCongestion_level(0);
        }
        else if(congestLevel.equals("여유"))
        {
            area.setCongestion_level(1);
        }
        else if(congestLevel.equals("보통"))
        {
            area.setCongestion_level(2);
        }
        else if(congestLevel.equals("약간 붐빔"))
        {
            area.setCongestion_level(3);
        }
        else {
            area.setCongestion_level(4);
        }
        return area;
    }
}
