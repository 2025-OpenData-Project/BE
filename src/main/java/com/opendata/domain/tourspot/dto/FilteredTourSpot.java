package com.opendata.domain.tourspot.dto;

import com.opendata.domain.tourspot.entity.TourSpot;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;

import java.time.LocalDateTime;
import java.util.List;

public record FilteredTourSpot(
        TourSpot tourSpot,
        LocalDateTime tourspotTm
) {}