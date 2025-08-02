package com.opendata.domain.tourspot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tourspot_tag")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TourSpotTag extends TourSpotAssociated{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourSpotTagId;

    @Column(name = "tourspot_category")
    private String tourSpotCategory;

}
