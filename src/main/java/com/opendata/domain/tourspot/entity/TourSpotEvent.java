package com.opendata.domain.tourspot.entity;

import com.opendata.global.commoncode.entity.CommonCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tourspot_event")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourSpotEvent extends TourSpotAssociated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourspotEventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_type_cd")
    private CommonCode eventTypeCd;

    private String eventNm;
    private String eventPrd;
    private String eventPlce;
    private Double eventLat;
    private Double eventLon;
    private String tourspotThmb;
    private String tourspotUrl;
}