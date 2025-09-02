package com.opendata.domain.tourspot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TourSpotMetaResponse {
    private Long tourspotId;
    private String tourspotNm;
    private String imageUrl;
    private String congestionLabel;
}
