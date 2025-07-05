package com.opendata.domain.course.entity;

import com.opendata.domain.tourspot.dto.AreaComponentDto;
import com.opendata.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "course")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
    //private User userId;

    @Builder.Default
    private boolean isLike = false;

    @Builder.Default
    private boolean isActive = false;
}
