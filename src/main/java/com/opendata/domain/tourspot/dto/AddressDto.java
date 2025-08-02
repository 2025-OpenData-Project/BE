package com.opendata.domain.tourspot.dto;

public record AddressDto(
        String addressKorNm,
        String addressDetail,
        String latitude,
        String longitude
) {}
