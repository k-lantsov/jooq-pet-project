package com.company.jooqpetproject.controller;

import com.company.jooqpetproject.dto.CountryDto;
import com.company.jooqpetproject.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid CountryDto countryDto) {
        countryService.insertCountry(countryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Country was saved");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CountryDto> getCountryInfo(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(countryService.getCountry(id));
    }

    @GetMapping
    public ResponseEntity<List<CountryDto>> getCountries() {
        return ResponseEntity.status(HttpStatus.OK).body(countryService.getCountries());
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody @Valid CountryDto countryDto) {
        countryService.updateCountry(countryDto);
        return ResponseEntity.status(HttpStatus.OK).body("Country was updated");
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return ResponseEntity.status(HttpStatus.OK).body("Country was deleted");
    }
}
