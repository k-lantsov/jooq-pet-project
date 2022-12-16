package com.company.jooqpetproject.controller;

import com.company.jooqpetproject.dto.CityDto;
import com.company.jooqpetproject.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid CityDto cityDto) {
        cityService.insertCity(cityDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("City was saved");
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        cityService.deleteCityById(id);
        return ResponseEntity.status(HttpStatus.OK).body("City was deleted");
    }
}
