package com.opendata.domain.tourspot.service;


import com.opendata.domain.address.cache.AddressCache;
import com.opendata.domain.address.entity.Address;
import com.opendata.domain.tourspot.api.AreaApi;
import com.opendata.domain.tourspot.dto.CityDataDto;

import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.TourSpotCurrentCongestion;
import com.opendata.domain.tourspot.entity.TourSpotFutureCongestion;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import com.opendata.domain.tourspot.mapper.CurrentCongestionMapper;
import com.opendata.domain.tourspot.mapper.FutureCongestionMapper;
import com.opendata.domain.tourspot.repository.FutureCongestionRepository;
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
    private final AddressCache addressCache;

    private final FutureCongestionMapper futureCongestionMapper;
    private final CurrentCongestionMapper currentCongestionMapper;

    //@Scheduled(cron = "0 */10 * * * *", zone = "Asia/Seoul")
    @Transactional
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

    @Transactional
    private TourSpot convertToEntity(CityDataDto cityDataDto) {
        if (cityDataDto.getCitydata() == null) {
            return null;
        }

        var data = cityDataDto.getCitydata();

        List<CityDataDto.LivePopulationStatus> livePopulationStatuses = data.getLivePopulationStatuses();
        //List<CityDataDto.EventData> eventDataList = data.getEventDataList();



        String tourspotNm = cityDataDto.getCitydata().getAreaName();

        Address address = addressCache.getByKorName(tourspotNm);
        TourSpot tourSpot = tourSpotRepository.findByName(tourspotNm)
                .orElseGet(() -> TourSpot.create(address, tourspotNm));


        /// 없을 떄는 저장 먼저 해서 tourspotId 생성
        if(tourSpot.getTourspotId() == null){
            tourSpotRepository.save(tourSpot);
        }

        List<TourSpotFutureCongestion> futureCongestions = new ArrayList<>();


        livePopulationStatuses.forEach(status -> {
            if (!status.getFCstPpltn().isEmpty()) {
                String curTime = status.getFCstPpltn().get(0).getFcstTime();
                TourSpotCurrentCongestion tourSpotCurrentCongestion = currentCongestionMapper.toEntity(status, curTime, tourSpot);
                tourSpot.addCurrentCongestion(tourSpotCurrentCongestion);
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
                    TourSpotFutureCongestion congestion = futureCongestionMapper.toTourSpotFutureCongestion(futureData, tourSpot, congestionLevel);
                    futureCongestions.add(congestion);
                }

            });
        });


        tourSpot.updateFutureCongestions(futureCongestions);


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
