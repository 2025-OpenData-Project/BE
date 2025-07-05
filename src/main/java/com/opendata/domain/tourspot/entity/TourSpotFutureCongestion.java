package com.opendata.domain.tourspot.entity;

import com.opendata.domain.tourspot.entity.enums.CongestionLevel;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tourspot_future_congestion")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TourSpotFutureCongestion extends TourSpotAssociated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long realTimeCongestionId;

    private String fcstTime;

    @Enumerated(EnumType.STRING)
    private CongestionLevel congestionLvl;

    public void assignCongestion(CongestionLevel level){
        this.congestionLvl = level;
    }
}