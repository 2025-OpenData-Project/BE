package com.opendata.domain.tourspot.entity;

import com.opendata.domain.address.entity.Address;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "tourspot_related")
@NoArgsConstructor
@AllArgsConstructor
public class TourSpotRelated{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private String tourSpotCode;

    private String tourSpotName;

    private String largeCategory;

    private String middleCategory;

    private double mapX;

    private double mapY;


}
