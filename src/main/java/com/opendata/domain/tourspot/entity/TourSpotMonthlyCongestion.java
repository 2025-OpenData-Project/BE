package com.opendata.domain.tourspot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


// TODO: OPEN API 응답 참고하여 리팩터링 필요.
@Entity
@Table(name = "tourspot_monthly_congestion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TourSpotMonthlyCongestion extends TourSpotAssociated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monthlyCongestionId;

    private Integer congestionLvl;
}
