package com.opendata.domain.apidata.entity;

import com.opendata.domain.apidata.dto.CityDataDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "area")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Area
{
    @Id
    private String id;
    private String name;//미리 저장
    private String category;//미리 저장
    private String description;//미리 저장
    private String image;//미리 저장

    private double latitude;//미리 저장
    private double longitude;//미리 저장
    private List<CityDataDto.EventData> events;
    private boolean indoor;//미리 저장
    private int congestion_level;
    private List<CityDataDto.FutureData> futures;
}
