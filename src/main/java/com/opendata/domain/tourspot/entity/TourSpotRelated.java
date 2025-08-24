package com.opendata.domain.tourspot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "tourspot_related")
@NoArgsConstructor
@AllArgsConstructor
public class TourSpotRelated{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tourSpotCode;

    private String tourSpotName;

    private String largeCategory;

    private String middleCategory;

    private String mapX;

    private String mapY;
}
