package com.opendata.domain.tourspot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourSpotRelatedDto
{
    @JsonProperty("response")
    private Response response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        @JsonProperty("body")
        private Body body;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        @JsonProperty("items")
        private Items items;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        @JsonProperty("item")
        private List<AddressItem> itemList;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressItem {
        @JsonProperty("baseYmd")
        private String baseYmd;

        @JsonProperty("tAtsNm")
        private String tAtsNm;

        @JsonProperty("areaCd")
        private String areaCd;

        @JsonProperty("areaNm")
        private String areaNm;

        @JsonProperty("signguCd")
        private String signguCd;

        @JsonProperty("signguNm")
        private String signguNm;

        @JsonProperty("rlteTatsNm")
        private String rlteTatsNm;


    }
}
