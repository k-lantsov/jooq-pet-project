package com.company.jooqpetproject.service;

import com.company.jooqpetproject.dto.CityDto;

import java.util.List;

public interface CityService {

    void insertCity(CityDto cityDto);

    Boolean getCityByNameAndCountryId(String name, Long countryId);

    Boolean deleteCityById(Long id);

    void deleteCityByCountryId(Long countryId);

    List<CityDto> getCitiesByCountryId(Long countryId);
}
