package com.opendata.domain.tourspot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "tourspot_related")
@NoArgsConstructor
@AllArgsConstructor
public class TourSpotRelated extends TourSpotAssociated
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_tourspot_id", nullable = false)
    private TourSpot relatedTourSpot;
}
