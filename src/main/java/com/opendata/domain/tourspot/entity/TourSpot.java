package com.opendata.domain.tourspot.entity;

import com.opendata.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



import java.util.List;

@Entity
@Table(name = "tourspot")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TourSpot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tourspotId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "address_id")
//    private Address address;

    private Long tourspotCategoryCd;
    private String tourspotNm;
    private String tourspotDsrb;
    private Boolean indoorYn;


    @OneToMany(mappedBy = "tourspot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourSpotImage> images;

    @OneToMany(mappedBy = "tourspot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourSpotEvent> events;

    @OneToMany(mappedBy = "tourspot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourSpotFutureCongestion> futureCongestions;

    @OneToMany(mappedBy = "tourspot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourSpotMonthlyCongestion> monthlyCongestions;

    protected TourSpot(String tourspotNm){
        this.tourspotNm = tourspotNm;
    }

    public static TourSpot create(String tourspotNm){
        return new TourSpot(tourspotNm);
    }

    public void addSubEntities(List<TourSpotFutureCongestion> futureCongestions){
        this.futureCongestions = futureCongestions;
    }
}