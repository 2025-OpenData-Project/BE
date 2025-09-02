package com.opendata.domain.tourspot.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opendata.domain.address.cache.AddressCache;
import com.opendata.domain.address.entity.Address;
import com.opendata.domain.tourspot.api.AreaApi;
import com.opendata.domain.tourspot.dto.CityDataDto;

import com.opendata.domain.tourspot.dto.MonthlyCongestionDto;
import com.opendata.domain.tourspot.dto.response.TourSpotDetailResponse;
import com.opendata.domain.tourspot.dto.response.TourSpotMetaResponse;
import com.opendata.domain.tourspot.entity.*;
import com.opendata.domain.tourspot.dto.TourSpotRelatedDto;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import com.opendata.domain.tourspot.entity.TourSpotMonthlyCongestion;
import com.opendata.domain.tourspot.mapper.*;

import com.opendata.domain.tourspot.repository.*;
import com.opendata.global.response.exception.GlobalException;
import com.opendata.global.response.status.ErrorStatus;
import com.opendata.global.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final TourSpotImageRepository tourSpotImageRepository;
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
    private final TourSpotMetaMapper tourSpotMetaMapper;

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


        return tourSpotDetailMapper.toResponse(
                tourSpot,
                tourSpotDetailMapper.toAddressDto(address),
                congestionLabel,
                tourSpotDetailMapper.toEventDtos(tourSpotEvents),
                tourSpotDetailMapper.toTagDtos(tourSpotTags)
        );
    }

    public TourSpotMetaResponse combineTourSpotMeta(Long tourspotId) {
        TourSpot tourSpot = tourSpotRepository.findById(tourspotId).orElseThrow();
        TourSpotCurrentCongestion tourSpotCurrentCongestion = currentCongestionRepository.findByTourSpotAndCurTime(tourSpot, DateUtil.getCurrentRoundedFormattedDateTime());
        Optional<TourSpotImage> tourSpotImageOpt = tourSpotImageRepository.findByTourSpot(tourSpot);

        String congestionLabel = null;
        if (tourSpotCurrentCongestion != null) {
            congestionLabel = tourSpotCurrentCongestion.getCongestionLvl().getCongestionLabel();
        }

        String imageUrl = null;
        if (tourSpotImageOpt.isPresent()){
            imageUrl = tourSpotImageOpt.get().getTourspotImgUrl();
        }


        return tourSpotMetaMapper.toResponse(
                tourSpot,
                imageUrl,
                congestionLabel
        );
    }

    @Scheduled(cron = "0 */10 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void fetchAllAreaAndSave() {
        List<String> areaNames = new AreaApi.AreaParam().getAreaInfos();
        List<CompletableFuture<CityDataDto>> futures = areaNames.stream()
                .map(cityDataService::fetchCityData)
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        List<CityDataDto> dtoList = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();

        // 미리 DB 조회
        List<TourSpot> existing = tourSpotRepository.findAll();
        Map<String, TourSpot> existingMap = existing.stream()
                .collect(Collectors.toMap(TourSpot::getTourspotNm, t -> t));

        // 없는 것만 생성
        List<TourSpot> newSpots = dtoList.stream()
                .map(dto -> convertToEntity(dto, existingMap)) // Optional<TourSpot>
                .filter(Optional::isPresent)                   // 값 있는 것만
                .map(Optional::get)                            // Optional -> TourSpot
                .toList();

        tourSpotRepository.saveAll(newSpots);
    }

    private Optional<TourSpot> convertToEntity(CityDataDto cityDataDto, Map<String, TourSpot> existingMap) {
        var data = cityDataDto.getCitydata();
        if (data == null) return Optional.empty();

        String tourspotNm = data.getAreaName();
        TourSpot tourSpot = existingMap.getOrDefault(
                tourspotNm,
                TourSpot.create(addressCache.getByKorName(tourspotNm), tourspotNm)
        );

        insertTourSpotEvents(data.getEventDataList(), tourSpot);
        insertCongestion(data.getLivePopulationStatuses(), tourSpot);

        return Optional.of(tourSpot);
    }

    private void insertTourSpotEvents(List<CityDataDto.EventData> eventDataList, TourSpot tourSpot) {
        eventDataList.forEach(eventData -> {
            if (!tourSpotEventRepository.existsByEventNameAndEventPeriod(eventData.getEventName(), eventData.getEventPeriod())){
                tourSpotEventMapper.toTourSpotEvent(eventData, tourSpot);
            }
        });
    }

    private void insertCongestion(List<CityDataDto.LivePopulationStatus> populationStatuses, TourSpot tourSpot){
        populationStatuses.forEach(
                populationStatus -> {
                    if (!populationStatus.getFCstPpltn().isEmpty()) {
                        String curTime = populationStatus.getFCstPpltn().get(0).getFcstTime();
                        currentCongestionMapper.toEntity(populationStatus, curTime, tourSpot);
                        populationStatus.getFCstPpltn().forEach(futureData -> {
                            String rawLevel = futureData.getFcstCongestLvl();
                            String rawFcstTime = futureData.getFcstTime();
                            CongestionLevel congestionLevel = CongestionLevel.resolve(rawLevel);


                            Optional<TourSpotFutureCongestion> existing = futureCongestionRepository
                                    .findByTourSpotIdAndFcstTime(tourSpot.getTourspotId(), rawFcstTime);

                            if (existing.isPresent()) {
                                existing.get().assignCongestion(congestionLevel);
                            } else {
                                futureCongestionMapper.toTourSpotFutureCongestion(futureData, tourSpot, congestionLevel);
                            }

                        });
                    }

                }
        );
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
    public void saveRelatedTourspot() {
        List<Address> addressList = addressCache.getAll();
        if (addressList.isEmpty()) {
            throw new GlobalException(ErrorStatus.ADDRESS_NOT_FOUND);
        }

        // 원본 Address를 함께 carry하기 위한 래퍼
        record Fetch(Address source, TourSpotRelatedDto dto) {}

        List<CompletableFuture<Fetch>> futures = addressList.stream()
                .map(addr -> tourSpotRelatedService
                        .fetchRelatedTourSpotData(addr.getArea().getAreaCodeId(), addr.getAddressKorNm())
                        .handle((dto, ex) -> {
                            if (ex != null) {
                                log.warn("연관 API 실패 [{}]: {}", addr.getAddressKorNm(), ex.toString());
                                return new Fetch(addr, null);
                            }
                            return new Fetch(addr, dto);
                        }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        for (CompletableFuture<Fetch> f : futures) {
            Fetch fr = f.join();
            Address sourceAddress = fr.source();
            TourSpotRelatedDto dto = fr.dto();

            // DTO/아이템 가드
            if (dto == null ||
                    dto.getResponse() == null ||
                    dto.getResponse().getBody() == null ||
                    dto.getResponse().getBody().getItems() == null ||
                    dto.getResponse().getBody().getItems().getItemList() == null) {
                log.warn("관광지 연관 데이터가 비어있음: {}", (Object) null);
                continue;
            }

            List<TourSpotRelatedDto.AddressItem> items =
                    dto.getResponse().getBody().getItems().getItemList();
            if (items.isEmpty()) {
                log.warn("관광지 연관 데이터가 비어있음(아이템 없음): 요청={}", sourceAddress.getAddressKorNm());
                continue;
            }

            List<TourSpotRelated> relatedTourSpots = items.stream()
                    .flatMap(data -> {
                        try {
                            // 1) 자치구 코드로 후보 Address 조회
                            String districtCode = data.getSignguCd();
                            if (districtCode == null || districtCode.isBlank()) return Stream.empty();

                            List<Address> candidates = addressCache.getByAreaId(districtCode);
                            if (candidates == null || candidates.isEmpty()) return Stream.empty();

                            // 2) 후보 Address 각각을 TourSpot으로 매핑 (없으면 생성)
                            return candidates.stream().flatMap(singleAddress -> {
                                try {
                                    return Stream.of(tourSpotRelatedMapper.toEntity(
                                            singleAddress,
                                            data.getHubTatsCd(),
                                            data.getHubTatsNm(),
                                            data.getHubCtgryLclsNm(),
                                            data.getHubCtgryMclsNm(),
                                            data.getMapX(),
                                            data.getMapY()));
                                } catch (Exception e) {
                                    log.warn("후보 Address 매핑 실패: {} - {}", singleAddress.getAddressKorNm(), e.toString());
                                    return Stream.<TourSpotRelated>empty();
                                }
                            });

                        } catch (Exception e) {
                            log.warn("연관 매핑 중 예외: {}", e.toString());
                            return Stream.empty();
                        }
                    }).toList();

            tourSpotRelatedRepository.saveAll(relatedTourSpots);

        }
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
