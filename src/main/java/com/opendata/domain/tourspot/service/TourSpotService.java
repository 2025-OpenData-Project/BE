package com.opendata.domain.tourspot.service;


import com.opendata.domain.tourspot.api.AreaApi;
import com.opendata.domain.tourspot.dto.CityDataDto;

import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotFutureCongestion;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import com.opendata.domain.tourspot.mapper.FutureCongestionMapper;
import com.opendata.domain.tourspot.repository.TourSpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourSpotService
{
    private final CityDataService cityDataService;
    private final TourSpotRepository tourSpotRepository;
    private final FutureCongestionMapper mapper;

    @Scheduled(cron = "0 */10 * * * *", zone = "Asia/Seoul")
    public void fetchAllAreaAndSave() {
        List<String> areaNames = new AreaApi.AreaParam().getAreaInfos();
        List<CompletableFuture<CityDataDto>> futures = areaNames.stream()
                .map(cityDataService::fetchCityData)
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();//끝날때까지 기다리기

        List<TourSpot> areaList = futures.stream()
                .map(CompletableFuture::join)
                .map(this::convertToEntity)
                .filter(Objects::nonNull)
                .toList();
        tourSpotRepository.saveAll(areaList);

    }

    private TourSpot convertToEntity(CityDataDto cityDataDto) {
        if (cityDataDto.getCitydata() == null) {
            return null;
        }

        var data = cityDataDto.getCitydata();

        List<CityDataDto.LivePopulationStatus> livePopulationStatuses = data.getLivePopulationStatuses();
        List<CityDataDto.EventData> eventDataList = data.getEventDataList();

        String congestLevel = livePopulationStatuses.get(0).getAreaCongestLvl();


        TourSpot tourSpot = TourSpot.create(cityDataDto.getCitydata().getAreaName());
        List<TourSpotFutureCongestion> futureCongestions = new ArrayList<>();

        livePopulationStatuses.forEach(status -> {
            status.getFCstPpltn().forEach(futureData -> {
                CongestionLevel congestionLevel = CongestionLevel.resolve(futureData.getFcstCongestLvl());
                TourSpotFutureCongestion congestion = mapper.mapWithExtra(futureData, tourSpot, congestionLevel);
                futureCongestions.add(congestion);
            });
        });

        tourSpot.addSubEntities(futureCongestions);

        return tourSpot;
    }
//    public List<AreaCongestionDto> mapToClosestTimeList(List<TourSpot> areas, String currentTime) {
//        return areas.stream()
//                .map(area -> area.getFutures().stream()
//                        .filter(f -> f.getFcstTime().compareTo(currentTime) <= 0)
//                        .max(Comparator.comparing(CityDataDto.FutureData::getFcstTime))
//                        .map(f -> new AreaCongestionDto(area.getName(), f.getFcstCongestLvl()))
//                        .orElse(null)
//                )
//                .filter(Objects::nonNull)
//                .toList();
//    }

//    public List<AreaCongestionDto> fetchAndConvertAreaCongestionDto(){
//        String currentTime = DateUtil.getCurrentFormattedDateTime();
//        return mapToClosestTimeList(areaRepository.findAreaWithCongestionByCurrentTime(currentTime), currentTime);
//    }

//    private TourSpotImage convertToTourSpotImages(TourSpot tourSpot, String thumbnail){
//        return TourSpotImage.builder()
//                .tourspotImgUrl(thumbnail)
//                .tourspot(tourSpot)
//                .tourspotRepImgYn(true)
//                .build();
//    }

//    private List<TourSpotEvent> convertToEvents(TourSpot tourSpot, List<CityDataDto.EventData> eventDataList){
//        List<TourSpotEvent> tourSpotEvents = new ArrayList<>();
//        eventDataList.forEach(
//                eventData -> {
//                    tourSpotEvents.add(eventData.toTourSpotEvent(tourSpot));
//                }
//        );
//        return tourSpotEvents;
//    }

//    private List<> convertToEvents(TourSpot tourSpot, List<CityDataDto.LivePopulationStatus> congestionData){
//        List<TourSpotFutureCongestion> tourSpotFutureCongestions = new ArrayList<>();
//        congestionData.forEach(
//                congestion -> {
//                    tourSpotFutureCongestions.add(congestion.toTourSpotEvent(tourSpot));
//                }
//        );
//        return tourSpotEvents;
//    }


}
