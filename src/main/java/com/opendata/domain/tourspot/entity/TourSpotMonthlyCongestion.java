package com.opendata.domain.tourspot.entity;

import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tourspot_monthly_congestion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TourSpotMonthlyCongestion extends TourSpotAssociated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monthlyCongestionId;

    private String date;

    @Enumerated(EnumType.STRING)
    private CongestionLevel congestionLvl;
}
