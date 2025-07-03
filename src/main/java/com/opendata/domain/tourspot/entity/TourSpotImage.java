package com.opendata.domain.tourspot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tourspot_image")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TourSpotImage extends TourSpotAssociated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourspotImgId;

    private String tourspotImgUrl;

    @Column(name = "tourspot_rep_img_yn")
    private Boolean isRepresentative;
}