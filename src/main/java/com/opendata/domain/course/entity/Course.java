package com.opendata.domain.course.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "courses")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    private String id;
    private List<String> places;
    private int placeCount;
    private String startTime;
    private String endTime;
    private boolean isFavorite;
}
