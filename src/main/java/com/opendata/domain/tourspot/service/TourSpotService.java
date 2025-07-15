package com.opendata.domain.tourspot.service;


import com.opendata.domain.address.cache.AddressCache;
import com.opendata.domain.address.entity.Address;
import com.opendata.domain.tourspot.api.AreaApi;
import com.opendata.domain.tourspot.dto.CityDataDto;

import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotCurrentCongestion;
import com.opendata.domain.tourspot.entity.TourSpotEvent;
import com.opendata.domain.tourspot.entity.TourSpotFutureCongestion;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import com.opendata.domain.tourspot.mapper.CurrentCongestionMapper;
import com.opendata.domain.tourspot.mapper.FutureCongestionMapper;
import com.opendata.domain.tourspot.mapper.TourSpotEventMapper;
import com.opendata.domain.tourspot.repository.FutureCongestionRepository;
import com.opendata.domain.tourspot.repository.TourSpotEventRepository;
import com.opendata.domain.tourspot.repository.TourSpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourSpotService
{
    private final CityDataService cityDataService;

    private final TourSpotRepository tourSpotRepository;
    private final FutureCongestionRepository futureCongestionRepository;
    private final TourSpotEventRepository tourSpotEventRepository;


    private final AddressCache addressCache;

    private final TourSpotEventMapper tourSpotEventMapper;
    private final FutureCongestionMapper futureCongestionMapper;
    private final CurrentCongestionMapper currentCongestionMapper;

    //@Scheduled(cron = "0 */10 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void fetchAllAreaAndSave() {
        List<String> areaNames = new AreaApi.AreaParam().getAreaInfos();
        List<CompletableFuture<CityDataDto>> futures = areaNames.stream()
                .map(cityDataService::fetchCityData)
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();



        List<TourSpot> areaList = futures.stream()
                .map(CompletableFuture::join)
                .map(this::convertToEntity)
                .filter(Objects::nonNull)
                .toList();

        tourSpotRepository.saveAll(areaList);
    }

    @Transactional
    private TourSpot convertToEntity(CityDataDto cityDataDto) {
        if (cityDataDto.getCitydata() == null) {
            return null;
        }

        var data = cityDataDto.getCitydata();

        List<CityDataDto.LivePopulationStatus> livePopulationStatuses = data.getLivePopulationStatuses();
        List<CityDataDto.EventData> eventDataList = data.getEventDataList();



        String tourspotNm = cityDataDto.getCitydata().getAreaName();

        Address address = addressCache.getByKorName(tourspotNm);

        TourSpot tourSpot = tourSpotRepository.findByName(tourspotNm)
                .orElseGet(() -> TourSpot.create(address, tourspotNm));


        /// 없을 떄는 저장 먼저 해서 tourspotId 생성
        if(tourSpot.getTourspotId() == null){
            tourSpotRepository.save(tourSpot);
        }

        insertTourSpotEvents(eventDataList, tourSpot);

        List<TourSpotFutureCongestion> futureCongestions = new ArrayList<>();


        livePopulationStatuses.forEach(status -> {
            if (!status.getFCstPpltn().isEmpty()) {
                String curTime = status.getFCstPpltn().get(0).getFcstTime();
                currentCongestionMapper.toEntity(status, curTime, tourSpot);
            }

            status.getFCstPpltn().forEach(futureData -> {
                String rawLevel = futureData.getFcstCongestLvl();
                String rawFcstTime = futureData.getFcstTime();
                CongestionLevel congestionLevel = CongestionLevel.resolve(rawLevel);


                Optional<TourSpotFutureCongestion> existing = futureCongestionRepository
                        .findByTourSpotIdAndFcstTime(tourSpot.getTourspotId(), rawFcstTime);

                if (existing.isPresent()) {
                    existing.get().assignCongestion(congestionLevel);
                    futureCongestions.add(existing.get());
                } else {
                    futureCongestionMapper.toTourSpotFutureCongestion(futureData, tourSpot, congestionLevel);
                }

            });
        });
        return tourSpot;
    }

    private void insertTourSpotEvents(List<CityDataDto.EventData> eventDataList, TourSpot tourSpot) {
        eventDataList.forEach(eventData -> {
            if (!tourSpotEventRepository.existsByEventNameAndEventPeriod(eventData.getEventName(), eventData.getEventPeriod())){
                tourSpotEventMapper.toTourSpotEvent(eventData, tourSpot);
            }
        });
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
}
