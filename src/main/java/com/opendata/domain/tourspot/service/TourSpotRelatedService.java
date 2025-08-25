package com.opendata.domain.tourspot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opendata.domain.address.entity.Address;
import com.opendata.domain.address.repository.AddressRepository;

import com.opendata.domain.tourspot.dto.TourSpotRelatedDto;
import com.opendata.domain.tourspot.dto.response.TourSpotRelatedResponse;
import com.opendata.domain.tourspot.repository.TourSpotRelatedRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourSpotRelatedService {
    private final WebClient openApiWebClient;
    private final ObjectMapper objectMapper;
    private final AddressRepository addressRepository;
    private final TourSpotRelatedRepository tourSpotRelatedRepository;

    @Value("${api.tour_api_congestion_key}")
    private String secretKey;



    public List<TourSpotRelatedResponse> fetchRelatedTourSpots(Long addressId){
        Address address = addressRepository.findById(addressId).orElseThrow();
        Optional<List<TourSpotRelatedResponse>> tourSpotRelatedListOptional = tourSpotRelatedRepository.findAllByAddress(address);

        return tourSpotRelatedListOptional.orElseGet(ArrayList::new);

    }

    public CompletableFuture<TourSpotRelatedDto> fetchRelatedTourSpotData(Long areaId, String tourSpotName) {
        try {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://apis.data.go.kr/B551011/LocgoHubTarService1/areaBasedList1")
                    .queryParam("serviceKey", secretKey)
                    .queryParam("pageNo", 1)
                    .queryParam("numOfRows", 5)
                    .queryParam("MobileOS", "WEB")
                    .queryParam("MobileApp", "Test")
                    .queryParam("areaCd", 11)
                    .queryParam("signguCd",areaId)
                    .queryParam("baseYm","202504")
                    .queryParam("_type", "json")
                    .build(true)
                    .toUri();
            log.info("test {}!!!", uri);

            return openApiWebClient.get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnNext(body -> log.warn("응답 본문:\n{}", body))
                    .map(json -> {
                        try {
                            return objectMapper.readValue(json, TourSpotRelatedDto.class);
                        } catch (Exception e) {
                            log.error("파싱 실패 (signguCd={}): {}", areaId, e.getMessage());
                            return null;
                        }
                    })
                    .onErrorResume(e -> {
                        log.error("요청 자체 실패 (signguCd={}): {}", areaId, e.getMessage());
                        return Mono.empty();
                    })
                    .toFuture();
        } catch (Exception e) {
            log.error("{},{} 실패했음!!!", tourSpotName, e.getMessage());
            return CompletableFuture.completedFuture(null);
        }

    }
}
