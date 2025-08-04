package com.opendata.domain.course.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opendata.domain.user.entity.User;
import com.opendata.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CourseComponent> courseComponents;

    private LocalDateTime startDtm;

    private LocalDateTime endDtm;

    @Builder.Default
    private boolean isLike = false;

    @Builder.Default
    private boolean isActive = false;

    public void assignStartDtm(LocalDateTime startDtm){
        this.startDtm = startDtm;
    }

    public void assignEndDtm(LocalDateTime endDtm){
        this.endDtm = endDtm;
    }
}
