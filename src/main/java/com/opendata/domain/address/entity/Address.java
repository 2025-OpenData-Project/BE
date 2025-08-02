package com.opendata.domain.address.entity;

import com.opendata.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @Column(name = "address_kor_nm")
    private String addressKorNm;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}