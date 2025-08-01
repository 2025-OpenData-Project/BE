package com.opendata.domain.tourspot.entity;

import com.opendata.domain.tourspot.entity.enums.CongestionLevel;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "tourspot_future_congestion")
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
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