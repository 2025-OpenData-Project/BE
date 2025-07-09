package com.opendata.domain.course.entity;

import com.opendata.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


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


//    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<CourseComponent> courseComponents;

    private LocalDateTime startDtm;
    private LocalDateTime endDtm;

    @Builder.Default
    private boolean isLike = false;

    @Builder.Default
    private boolean isActive = false;
}
