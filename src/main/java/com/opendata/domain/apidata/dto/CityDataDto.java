package com.opendata.domain.apidata.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


//데이터 파싱 부부ㄴ
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityDataDto
{
    @JsonProperty("CITYDATA")
    private CityData citydata;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CityData
    {
        @JsonProperty("AREA_NM")
        private String areaName;
        @JsonProperty("EVENT_STTS")
        private List<EventData> eventDataList;
        @JsonProperty("LIVE_PPLTN_STTS")
        private List<LivePopulationStatus> livePopulationStatuses;


    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LivePopulationStatus
    {
        @JsonProperty("AREA_CONGEST_LVL")
        private String areaCongestLvl;
        @JsonProperty("FCST_PPLTN")
        private List<FutureData> fCstPpltn;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FutureData
    {
        @JsonProperty("FCST_TIME")
        private String fcstTime;
        @JsonProperty("FCST_CONGEST_LVL")
        private String fcstCongestLvl;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EventData
    {
        @JsonProperty("EVENT_NM")
        private String eventName;

        @JsonProperty("EVENT_PERIOD")
        private String eventPeriod;
        @JsonProperty("EVENT_PLACE")
        private String eventPlace;
        @JsonProperty("EVENT_X")
        private String eventX;
        @JsonProperty("EVENT_Y")
        private String eventY;
        @JsonProperty("THUMBNAIL")
        private String thumbnail;
        @JsonProperty("URL")
        private String url;

    }

}
