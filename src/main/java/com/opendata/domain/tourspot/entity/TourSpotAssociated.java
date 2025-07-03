package com.opendata.domain.tourspot.entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class TourSpotAssociated {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourspot_id")
    private TourSpot tourspot;

    public void assignTourSpot(TourSpot tourspot) {
        this.tourspot = tourspot;
    }
}
