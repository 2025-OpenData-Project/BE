package com.opendata.domain.tourspot.entity;

import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tourspot_monthly_congestion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TourSpotMonthlyCongestion extends TourSpotAssociated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monthlyCongestionId;

    private String baseYmd;

    @Enumerated(EnumType.STRING)
    private CongestionLevel congestionLvl;
}
