package com.opendata.domain.tourspot.entity;

import com.opendata.global.commoncode.entity.CommonCode;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "congestion_lvl_cd")
    private CommonCode congestionLvl;

    public void assignCongestion(CommonCode code){
        this.congestionLvl = code;
    }
}