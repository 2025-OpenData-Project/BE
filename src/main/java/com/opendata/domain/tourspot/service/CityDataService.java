package com.opendata.domain.tourspot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opendata.domain.tourspot.dto.CityDataDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
    @Value("${api.seoul_city_data_key}")
    private String secretKey;


    @Async
    public CompletableFuture<CityDataDto> fetchCityData(String areaName)
    {
        try {
            String url = String.format("%s/%s/json/citydata/1/5/%s",
                    BASE_URL,
                    secretKey,
                    URLEncoder.encode(areaName, StandardCharsets.UTF_8)
            );
            log.info("test {}!!!", url);

            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(body -> {
                        if (body.trim().startsWith("{")) {
                            try {
                                return new ObjectMapper().readValue(body, CityDataDto.class);
                            } catch (Exception ex) {
                                log.error("JSON 파싱 실패: {}", ex.getMessage());
                                return new CityDataDto();
                            }
                        } else {
                            log.warn("⚠️ XML 응답: {}", body);
                            return new CityDataDto();
                        }
                    })
                    .toFuture();

        } catch (Exception e) {
            log.error("{},{} 실패했음!!!", areaName, e.getMessage());
            return CompletableFuture.completedFuture(null);
        }

    }


}
