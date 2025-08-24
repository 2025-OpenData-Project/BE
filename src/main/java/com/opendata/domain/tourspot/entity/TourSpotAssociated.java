package com.opendata.domain.tourspot.entity;

import com.opendata.global.entity.BaseEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class TourSpotAssociated extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourspot_id")
    private TourSpot tourspot;

    public void assignTourSpot(TourSpot tourspot) {
        this.tourspot = tourspot;
    }
}
