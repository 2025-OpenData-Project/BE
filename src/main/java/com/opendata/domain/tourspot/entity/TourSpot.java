package com.opendata.domain.tourspot.entity;

import com.opendata.domain.address.entity.Address;
import com.opendata.global.entity.BaseEntity;
import jakarta.persistence.*;

import lombok.*;



import java.util.List;

@Entity
@Table(name = "tourspot", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueTourSpot", columnNames = {"tourspot_id", "address_id"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TourSpot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourspotId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private String tourspotNm;

    @OneToMany(mappedBy = "tourspot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourSpotCurrentCongestion> currentCongestions;

    @OneToMany(mappedBy = "tourspot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourSpotImage> images;

    @OneToMany(mappedBy = "tourspot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourSpotEvent> events;

    @OneToMany(mappedBy = "tourspot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourSpotFutureCongestion> futureCongestions;

    @OneToMany(mappedBy = "tourspot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourSpotMonthlyCongestion> monthlyCongestions;

    @OneToMany(mappedBy = "tourspot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourSpotRelated> relatedSpots;


    protected TourSpot(Address address, String tourspotNm){
        this.address = address;
        this.tourspotNm = tourspotNm;
    }

    public static TourSpot create(Address address, String tourspotNm){
        return new TourSpot(address, tourspotNm);
    }


    public void addMonthlyCongestion(TourSpotMonthlyCongestion congestion) {
        this.monthlyCongestions.add(congestion);
    }


    public void updateMonthlyCongestions(List<TourSpotMonthlyCongestion> newOnes) {
        this.monthlyCongestions.clear();
        newOnes.forEach(this::addMonthlyCongestion);
    }
    public void updateTourSpotRelated(List<TourSpotRelated> newOnes) {
        this.relatedSpots.clear();
        newOnes.forEach(this::addRelatedSpots);
    }

    public void addFutureCongestion(TourSpotFutureCongestion congestion) {
        this.futureCongestions.add(congestion);
    }

    public void addCurrentCongestion(TourSpotCurrentCongestion congestion) {
        this.currentCongestions.add(congestion);
    }

    public void addEvent(TourSpotEvent event) { this.events.add(event); }

    public void addRelatedSpots(TourSpotRelated newOnes) {
        this.relatedSpots.add(newOnes);
    }

}