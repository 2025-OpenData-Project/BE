package com.opendata.domain.tourspot.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opendata.domain.address.cache.AddressCache;
import com.opendata.domain.address.entity.Address;
import com.opendata.domain.tourspot.api.AreaApi;
import com.opendata.domain.tourspot.dto.CityDataDto;

import com.opendata.domain.tourspot.dto.MonthlyCongestionDto;
import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import com.opendata.domain.tourspot.entity.*;
import com.opendata.domain.tourspot.dto.TourSpotRelatedDto;
import com.opendata.domain.tourspot.entity.*;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import com.opendata.domain.tourspot.entity.TourSpotMonthlyCongestion;
import com.opendata.domain.tourspot.mapper.*;

import com.opendata.domain.tourspot.repository.*;
import com.opendata.global.response.exception.GlobalException;
import com.opendata.global.response.status.ErrorStatus;
import com.opendata.global.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourSpotService
{
    private final CityDataService cityDataService;
    private final MonthlyCongestionService govCongestionService;
    private final TourSpotRelatedService tourSpotRelatedService;

    private final TourSpotRepository tourSpotRepository;
    private final TourSpotEventRepository tourSpotEventRepository;
    private final TourSpotRelatedRepository tourSpotRelatedRepository;
    private final TourSpotTagRepository tourSpotTagRepository;
    private final CurrentCongestionRepository currentCongestionRepository;
    private final FutureCongestionRepository futureCongestionRepository;
    private final MonthlyCongestionRepository monthlyCongestionRepository;

    private final AddressCache addressCache;

    private final TourSpotEventMapper tourSpotEventMapper;
    private final FutureCongestionMapper futureCongestionMapper;
    private final CurrentCongestionMapper currentCongestionMapper;
    private final MonthlyCongestionMapper monthlyCongestionMapper;
    private final TourSpotRelatedMapper tourSpotRelatedMapper;
    private final TourSpotDetailMapper tourSpotDetailMapper;

    public TourSpotDetailResponse combineTourSpotDetail(Long tourspotId) throws JsonProcessingException {
        TourSpot tourSpot = tourSpotRepository.findById(tourspotId).orElseThrow();
        Address address = addressCache.getByKorName(tourSpot.getTourspotNm());
        TourSpotCurrentCongestion tourSpotCurrentCongestion = currentCongestionRepository.findByTourSpotAndCurTime(tourSpot, DateUtil.getCurrentRoundedFormattedDateTime());
        List<TourSpotEvent> tourSpotEvents = tourSpotEventRepository.findAllByTourSpot(tourSpot);
        List<TourSpotTag> tourSpotTags = tourSpotTagRepository.findAllByTourSpot(tourSpot);

        String congestionLabel = null;
        if (tourSpotCurrentCongestion != null) {
            congestionLabel = tourSpotCurrentCongestion.getCongestionLvl().getCongestionLabel();
        }


        TourSpotDetailResponse t = tourSpotDetailMapper.toResponse(
                tourSpot,
                tourSpotDetailMapper.toAddressDto(address),
                congestionLabel,
                tourSpotDetailMapper.toEventDtos(tourSpotEvents),
                tourSpotDetailMapper.toTagDtos(tourSpotTags)
        );
        System.out.println(new ObjectMapper().writeValueAsString(t));
        return t;
    }

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

    @Transactional
    public void fetchAllOrganTourSpotAndSave()
    {
        List<Address> addressList = addressCache.getAll();
        if(addressList.isEmpty()){
            throw new GlobalException(ErrorStatus.ADDRESS_NOT_FOUND);
        }
        List<TourSpot> tourSpotList =new ArrayList<>();
        for(Address address : addressList){
            TourSpot tourSpot = tourSpotRepository.findByName(address.getAddressKorNm())
                    .orElseGet(() -> TourSpot.create(address, address.getAddressKorNm()));
            tourSpotList.add(tourSpot);
        }
        tourSpotRepository.saveAll(tourSpotList);
    }

    @Transactional
    public void updateMonthlyCongestion()
    {
        //캐시에서 어드레스 뽑아오깅
        List<Address> addressList = addressCache.getAll();
        if(addressList.isEmpty()){
            throw new GlobalException(ErrorStatus.ADDRESS_NOT_FOUND);
        }
        //쓰레드 터짐 오류로 인해 제한
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<CompletableFuture<MonthlyCongestionDto>> monthlyCongestionDtoList = addressList.stream()
                .map(address -> CompletableFuture.supplyAsync(() ->
                                govCongestionService
                                        .fetchGovCongestionData(address.getArea().getAreaCodeId(), address.getAddressKorNm())
                                        .join(),
                        executor
                ))
                .toList();
        //기다리기
        CompletableFuture.allOf(monthlyCongestionDtoList.toArray(new CompletableFuture[0])).join();


        List<TourSpot> updatedSpots = monthlyCongestionDtoList.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .map(dto -> {
                    //api에서 관광지 이름 뜯어와서 투어스팟 불러오기
                    String spotName = extractSpotNameFromDto(dto);

                    return tourSpotRepository.findByName(spotName)
                            .map(tourSpot -> {
                                List<TourSpotMonthlyCongestion> monthlyList =
                                        convertMonthlyDtoToEntities(dto, tourSpot);
                                tourSpot.updateMonthlyCongestions(monthlyList);
                                return tourSpot;
                            })
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .toList();

        tourSpotRepository.saveAll(updatedSpots);



    }

    private List<TourSpotMonthlyCongestion> convertMonthlyDtoToEntities(MonthlyCongestionDto monthlyCongestionDto, TourSpot tourSpot )
    {
        if (monthlyCongestionDto == null || tourSpot == null) {
            return List.of();
        }
        return monthlyCongestionDto.getResponse().getBody().getItems().getItemList().stream()
                .map(item -> monthlyCongestionMapper.toMonthlyCongestion(item, tourSpot))
                .filter(Objects::nonNull)
                .toList();

    }
    private String extractSpotNameFromDto(MonthlyCongestionDto dto) {
        return Optional.ofNullable(dto)
                .map(MonthlyCongestionDto::getResponse)
                .map(MonthlyCongestionDto.Response::getBody)
                .map(MonthlyCongestionDto.Body::getItems)
                .map(MonthlyCongestionDto.Items::getItemList)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0).getTAtsNm())
                .orElse(null);
    }

    @Transactional
    public void saveRelatedTourspot()
    {
        List<Address> addressList = addressCache.getAll();
        if(addressList.isEmpty()){
            throw new GlobalException(ErrorStatus.ADDRESS_NOT_FOUND);
        }
        List<CompletableFuture<TourSpotRelatedDto>> tourSpotRelatedDtoList = addressList.stream()
                .map(address-> tourSpotRelatedService.fetchRelatedTourSpotData(address.getArea().getAreaCodeId(),address.getAddressKorNm()))
                .toList();

        CompletableFuture.allOf(tourSpotRelatedDtoList.toArray(new CompletableFuture[0])).join();


        tourSpotRelatedDtoList.forEach(future->{
            try
            {
                TourSpotRelatedDto dto = future.join();
                if (dto == null || dto.getResponse() == null || dto.getResponse().getBody() == null
                        || dto.getResponse().getBody().getItems() == null || dto.getResponse().getBody().getItems().getItemList() == null) {
                    log.warn("관광지 연관 데이터가 비어있음: {}", dto);
                    return;
                }
                List<TourSpotRelatedDto.AddressItem> items = dto.getResponse()
                        .getBody()
                        .getItems()
                        .getItemList();


                String tourSpotName = items.get(0).getTAtsNm();
                TourSpot currentTourSpot = tourSpotRepository.findByName(tourSpotName)
                        .orElseThrow(() -> new GlobalException(ErrorStatus.TOURSPOT_NOT_FOUND));
                List<TourSpotRelated> tourSpotRelateds= items.stream()
                        .map(data -> {
                            try
                            {
                                TourSpot tourSpot=tourSpotRepository.findByName(data.getRlteTatsNm())
                                        .orElseThrow(() -> new GlobalException(ErrorStatus.TOURSPOT_NOT_FOUND));
                                TourSpotRelated tourSpotRelated = tourSpotRelatedMapper.toEntity(currentTourSpot, tourSpot);

                                return tourSpotRelated;

                            }catch (GlobalException globalException){
                                log.warn("해당 관광지가 없음-공사 api");
                                return null;
                            }

                        })
                        .filter(Objects::nonNull)
                        .toList();

                if (!tourSpotRelateds.isEmpty()) {
                    currentTourSpot.updateTourSpotRelated(tourSpotRelateds);
                    tourSpotRepository.save(currentTourSpot);
                }
            }
            catch(GlobalException globalException){
                log.warn("해당 관광지가 없음-tourSpot 관광지");
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
