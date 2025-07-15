package com.opendata.domain.tourspot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tourspot_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourSpotEvent extends TourSpotAssociated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourspotEventId;

    private String eventName;
    private String eventPeriod;
    private String eventPlace;
    private Double eventX;
    private Double eventY;
    private String tourspotThumbnail;
    private String tourspotUrl;
}