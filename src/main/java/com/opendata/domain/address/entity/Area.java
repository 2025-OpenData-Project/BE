package com.opendata.domain.address.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "area")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long areaCodeId;

    private String areaSigungu;

    private Long parentAreaCodeId;
}
