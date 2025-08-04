package com.opendata.domain.course.entity;


import com.opendata.domain.tourspot.entity.TourSpot;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_component")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourspot_id")
    private TourSpot tourSpot;

    @Column(name = "tourspot_tm")
    private LocalDateTime tourspotTm;

    public void assignCourse(Course course){
        this.course = course;
    }
}
