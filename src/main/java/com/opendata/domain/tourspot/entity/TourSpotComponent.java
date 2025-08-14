package com.opendata.domain.tourspot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tourspot_component")
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 성능 문제로 FK만 둠
public class TourSpotComponent
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long tourSpotId;


    public static TourSpotComponent toTourSpotComponent(Long userId, Long tourSpotId) {
        return TourSpotComponent.builder()
                .tourSpotId(tourSpotId)
                .userId(userId)
                .build();
    }


}
