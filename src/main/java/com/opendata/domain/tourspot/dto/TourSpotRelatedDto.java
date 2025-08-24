package com.opendata.domain.tourspot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonToken;
import lombok.Data;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourSpotRelatedDto {

    @JsonProperty("response")
    private Response response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        @JsonProperty("header")
        private Header header;

        @JsonProperty("body")
        private Body body;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        @JsonProperty("items")
        @JsonDeserialize(using = ItemsOrEmptyStringDeserializer.class)
        private Items items;

        @JsonProperty("numOfRows")
        private Integer numOfRows;

        @JsonProperty("pageNo")
        private Integer pageNo;

        @JsonProperty("totalCount")
        private Integer totalCount;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        @JsonProperty("item")
        private List<AddressItem> itemList = Collections.emptyList();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressItem {
        // 예: 202504
        @JsonProperty("baseYm")
        private String baseYm;

        // 좌표 (문자열로 두면 파싱 실패 리스크↓)
        @JsonProperty("mapX")
        private String mapX;

        @JsonProperty("mapY")
        private String mapY;

        // 광역단위 (예: 11, 서울특별시)
        @JsonProperty("areaCd")
        private String areaCd;

        @JsonProperty("areaNm")
        private String areaNm;

        // 자치구 (예: 11140, 중구)
        @JsonProperty("signguCd")
        private String signguCd;

        @JsonProperty("signguNm")
        private String signguNm;

        // 거점 코드/명
        @JsonProperty("hubTatsCd")
        private String hubTatsCd;

        @JsonProperty("hubTatsNm")
        private String hubTatsNm;

        // 분류
        @JsonProperty("hubCtgryLclsNm")
        private String hubCtgryLclsNm;

        @JsonProperty("hubCtgryMclsNm")
        private String hubCtgryMclsNm;

        // 순위
        @JsonProperty("hubRank")
        private String hubRank;
    }

    /**
     * "items": "" 처럼 빈 문자열이 내려올 때를 대비한 디시리얼라이저
     * "" → 빈 Items (itemList = [])
     * 객체 → 정상 파싱
     */
    public static class ItemsOrEmptyStringDeserializer extends JsonDeserializer<Items> {
        @Override
        public Items deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken t = p.currentToken();
            if (t == JsonToken.VALUE_STRING) {
                // "", "   " 모두 빈 리스트로 취급
                String s = p.getText();
                Items i = new Items();
                i.setItemList(Collections.emptyList());
                return i;
            }
            // 정상 객체일 경우 표준 역직렬화
            return p.getCodec().readValue(p, Items.class);
        }
    }
}