package com.opendata.domain.tourspot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opendata.domain.tourspot.dto.CityDataDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static com.opendata.domain.tourspot.api.AreaApi.AreaEndPoint.BASE_URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityDataService
{
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    @Value("${api.seoul_city_data_key}")
    private String secretKey;


    @Async
    public CompletableFuture<CityDataDto> fetchCityData(String areaName) {
        try {
            String url = String.format("%s/%s/json/citydata/1/5/%s",
                    BASE_URL, secretKey,
                    URLEncoder.encode(areaName, StandardCharsets.UTF_8));

            log.info("fetching citydata: {}", url);

            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(body -> parseBody(body, areaName))   // 여기서 null 리턴 안 함
                    .onErrorResume(ex -> {
                        log.warn("⚠️ fetchCityData error for area={}: {}", areaName, ex.getMessage());
                        return Mono.empty(); // null 대신 빈 Mono
                    })
                    .toFuture();

        } catch (Exception e) {
            log.error("fetchCityData fatal error area={}: {}", areaName, e.getMessage());
            return CompletableFuture.completedFuture(null);
        }
    }

    private CityDataDto parseBody(String body, String areaName) {
        if (body.trim().startsWith("{")) {
            try {
                return objectMapper.readValue(body, CityDataDto.class);
            } catch (Exception ex) {
                log.error("JSON 파싱 실패 area={} err={}", areaName, ex.getMessage());
                return null;
            }
        } else {
            log.warn("⚠️ XML 응답 area={}: {}", areaName, body);
            return null;
        }
    }


}
