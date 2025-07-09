package com.opendata.domain.tourspot.entity;

import com.opendata.domain.address.entity.Address;
import com.opendata.domain.tourspot.entity.enums.CongestionLevel;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    private CongestionLevel congestionLevel;

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

    protected TourSpot(Address address, String tourspotNm, CongestionLevel congestionLevel){
        this.address = address;
        this.tourspotNm = tourspotNm;
        this.congestionLevel = congestionLevel;
    }

    public static TourSpot create(Address address, String tourspotNm, CongestionLevel congestionLevel){
        return new TourSpot(address, tourspotNm, congestionLevel);
    }

    public void addSubEntities(List<TourSpotFutureCongestion> futureCongestions){
        this.futureCongestions = futureCongestions;
    }
}