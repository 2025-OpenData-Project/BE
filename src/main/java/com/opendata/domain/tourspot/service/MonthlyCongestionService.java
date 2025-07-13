package com.opendata.domain.tourspot.service;


import com.opendata.domain.tourspot.dto.MonthlyCongestionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonthlyCongestionService
{
    private final WebClient webClient;

    @Value("${api.tour_api_congestion_key}")
    private String secretKey;

    @Async
    public CompletableFuture<MonthlyCongestionDto> fetchGovCongestionData(Long addressId, String tourSpotName)
    {
        try {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("http://apis.data.go.kr/B551011/TatsCnctrRateService/tatsCnctrRatedList")
                    .queryParam("serviceKey",secretKey)
                    .queryParam("pageNo", 1)
                    .queryParam("numOfRows", 100)
                    .queryParam("MobileOS", "WEB")
                    .queryParam("MobileApp", "Test")
                    .queryParam("areaCd", 11)
                    .queryParam("tAtsNm",addressId.toString())
                    .queryParam("signguCd",tourSpotName)
                    .queryParam("_type", "json")
                    .build(true) // true: 자동 인코딩 방지
                    .toUri();
            log.info("test {}!!!", uri);

            return webClient.get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(MonthlyCongestionDto.class)
                    .toFuture();
        } catch (Exception e) {
            log.error("{},{} 실패했음!!!", tourSpotName, e.getMessage());
            return CompletableFuture.completedFuture(null);
        }

    }
}
