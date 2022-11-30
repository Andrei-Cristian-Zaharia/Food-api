package com.licenta.food.controllers;

import com.licenta.food.enums.MeasurementUnit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/utils")
public class UtilsController {

    @GetMapping("/measurements")
    public @ResponseBody ResponseEntity<List<String>> getMeasurements() {

        return ResponseEntity.ok().body(Stream.of(MeasurementUnit.values())
                .map(Enum::name)
                .collect(Collectors.toList()));
    }
}
