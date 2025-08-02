//package com.opendata.domain.tourspot.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.opendata.domain.address.cache.AddressCache;
//import com.opendata.domain.address.entity.Address;
//import com.opendata.domain.address.repository.AddressRepository;
//import com.opendata.domain.tourspot.entity.TourSpot;
//import com.opendata.domain.tourspot.entity.TourSpotTag;
//import com.opendata.domain.tourspot.repository.TourSpotRepository;
//import com.opendata.domain.tourspot.repository.TourSpotTagRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class TourSpotGeoCodeService {
//
//    private static final String KAKAO_API_KEY = "KakaoAK 799fc99c89927abb8832e201e58c6a88";
//    private static final String KAKAO_API_BASE = "https://dapi.kakao.com";
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    private final AddressRepository addressRepository;
//    private final TourSpotRepository tourSpotRepository;
//    private final TourSpotTagRepository tourSpotTagRepository;
//
//    private final WebClient webClient = WebClient.builder()
//            .baseUrl(KAKAO_API_BASE)
//            .defaultHeader("Authorization", KAKAO_API_KEY)
//            .build();
//
//    @Transactional
//    public void processTourSpots() {
//        List<Address> addresses = addressRepository.findAll();
//        for (Address addr : addresses) {
//            String query = "서울 " + addr.getAddressKorNm();
//
//            // Kakao API 호출
//            JsonNode doc = callKakaoApi(query);
//
//            if (doc == null || doc.isMissingNode() || doc.isEmpty()) {
//                System.out.printf("-- No result: %s%n", addr.getAddressKorNm());
//                sleepQuietly(250);
//                continue;
//            }
//
//            // 좌표/주소/카테고리 추출
//            String addressName = doc.path("address_name").asText(null);
//            String rawCategory = doc.path("category_name").asText("");
//
//            if (addressName != null) {
//                addr.setAddressDetail(addressName);
//                addressRepository.save(addr);
//            }
//
//            // 기존 TourSpot 가져오기 (주소 기준)
//            Optional<TourSpot> maybeTourSpot = tourSpotRepository.findByAddress(addr);
//            if (maybeTourSpot.isEmpty()) {
//                System.out.printf("-- TourSpot 없음: %s%n", addr.getAddressKorNm());
//                sleepQuietly(250);
//                continue;
//            }
//            TourSpot tourSpot = maybeTourSpot.get();
//
//            // 카테고리 파싱
//            List<String> categories = Arrays.stream(rawCategory.split(">"))
//                    .map(String::trim)
//                    .flatMap(part -> Arrays.stream(part.split(",")))
//                    .map(String::trim)
//                    .filter(s -> !s.isEmpty())
//                    .distinct()
//                    .collect(Collectors.toList());
//
//            for (String category : categories) {
//                // 중복 태그 방지하려면 findExisting or unique constraint 검사 추가 가능
//                TourSpotTag tag = TourSpotTag.builder()
//                        .tourSpotCategory(category)
//                        .build();
//                tag.assignTourSpot(tourSpot);
//                tourSpotTagRepository.save(tag);
//            }
//
//            sleepQuietly(250);
//        }
//
//        System.out.println("✅ All processing done.");
//    }
//
//    private JsonNode callKakaoApi(String query) {
//        try {
//            var responseEntity = webClient.get()
//                    .uri(uriBuilder ->
//                            uriBuilder
//                                    .path("/v2/local/search/keyword.json")
//                                    .queryParam("query", query)
//                                    .build()
//                    )
//                    .retrieve()
//                    .onStatus(status -> !status.is2xxSuccessful(), clientResponse -> {
//                        System.err.printf("[KakaoAPI] 비정상 상태 코드 %s for '%s'%n", clientResponse.statusCode(), query);
//                        return clientResponse.createException();
//                    })
//                    .bodyToMono(String.class)
//                    .block();
//
//            if (responseEntity == null) {
//                System.err.printf("[KakaoAPI] 빈 바디 응답: '%s'%n", query);
//                return null;
//            }
//
//            JsonNode root = mapper.readTree(responseEntity);
//            System.out.printf("[KakaoAPI] full response for '%s': %s%n", query, root.toString());
//
//            JsonNode docs = root.path("documents");
//            if (docs.isArray() && docs.size() > 0) {
//                return docs.get(0);
//            } else {
//                System.out.printf("[KakaoAPI] documents 비어있음 for '%s'%n", query);
//                return null;
//            }
//        } catch (Exception e) {
//            System.err.printf("-- Kakao API error for '%s': %s%n", query, e.getMessage());
//            return null;
//        }
//    }
//
//    private void sleepQuietly(long ms) {
//        try {
//            Thread.sleep(ms);
//        } catch (InterruptedException ignored) { }
//    }
//}