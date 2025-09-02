package com.opendata.domain.tourspot.dto.response;

import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TourSpotMetaResponse {
    private Long tourspotId;
    private String tourspotNm;
    private String imageUrl;
    private String congestionLabel;

    public TourSpotMetaResponse(Long id, String name, String imageUrl, CongestionLevel congestionLvl) {
        this.tourspotId = id;
        this.tourspotNm = name;
        this.imageUrl = imageUrl;
        this.congestionLabel = congestionLvl.getCongestionLabel();
    }
}
