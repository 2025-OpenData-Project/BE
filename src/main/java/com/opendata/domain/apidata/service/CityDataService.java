package com.opendata.domain.apidata.service;

import com.opendata.domain.apidata.dto.CityDataDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static com.opendata.domain.apidata.api.AreaApi.AreaEndPoint.BASE_URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityDataService
{
    private final WebClient webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(config -> config
                            .defaultCodecs()
                            .maxInMemorySize(1024 * 1024)) // 1MB 제한
                    .build())
            .build();
    @Value("${api_secret}")
    private String secretKey;


    @Async
    @Scheduled(cron = "0 */10 * * * *", zone = "Asia/Seoul")
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
                    .bodyToMono(CityDataDto.class)
                    .toFuture();
        } catch (Exception e) {
            log.error("{},{} 실패했음!!!", areaName, e.getMessage());
            return CompletableFuture.completedFuture(null);
        }

    }


}
