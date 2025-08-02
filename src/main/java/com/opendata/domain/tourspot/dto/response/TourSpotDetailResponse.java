package com.opendata.domain.tourspot.dto.response;


import com.opendata.domain.tourspot.dto.AddressDto;
import com.opendata.domain.tourspot.dto.TourSpotEventDto;
import com.opendata.domain.tourspot.dto.TourSpotTagDto;
import com.opendata.domain.tourspot.entity.TourSpotEvent;
import com.opendata.domain.tourspot.entity.TourSpotTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourSpotDetailResponse {
    private String tourspotNm;
    private AddressDto address;
    private String congestionLabel;
    private List<TourSpotEventDto> tourSpotEvents;
    private List<TourSpotTagDto> tourSpotTags;
}
