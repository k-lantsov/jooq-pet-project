package com.company.jooqpetproject.service;

import com.company.jooqpetproject.dto.CountryDto;

import java.util.List;

public interface CountryService {

    void insertCountry(CountryDto country);

    List<CountryDto> getCountries();

    CountryDto getCountry(Long id);

    void updateCountry(CountryDto countryDto);

    void deleteCountry(Long id);
}
