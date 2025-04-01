package com.opendata.domain.apidata.controller;

import com.opendata.domain.apidata.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/area")
public class AreaController
{
    private final AreaService areaService;

    @GetMapping("/get")
    public ResponseEntity<Void> getArea()
    {
        areaService.fetchAllAreaAndSave();
        return ResponseEntity.ok().build();
    }
}
