package com.opendata.domain.tourspot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opendata.domain.tourspot.dto.MonthlyCongestionDto;
import com.opendata.domain.tourspot.dto.TourSpotRelatedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourSpotRelatedService
{
    private final WebClient openApiWebClient;
    private final ObjectMapper objectMapper;

    @Value("${api.tour_api_congestion_key}")
    private String secretKey;


    public CompletableFuture<TourSpotRelatedDto> fetchRelatedTourSpotData(Long areaId, String tourSpotName)
    {
        String encodedName = URLEncoder.encode(tourSpotName, StandardCharsets.UTF_8);
        try {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://apis.data.go.kr/B551011/TarRlteTarService1/searchKeyword1")
                    .queryParam("serviceKey", secretKey)
                    .queryParam("pageNo", 1)
                    .queryParam("numOfRows", 5)
                    .queryParam("MobileOS", "WEB")
                    .queryParam("MobileApp", "Test")
                    .queryParam("areaCd", 11)
                    .queryParam("signguCd",areaId)
                    .queryParam("keyword",encodedName)
                    .queryParam("baseYm","202503")
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
