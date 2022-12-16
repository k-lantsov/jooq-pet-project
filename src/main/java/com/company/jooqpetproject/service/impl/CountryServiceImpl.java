package com.company.jooqpetproject.service.impl;

import com.company.jooqpetproject.dto.CityDto;
import com.company.jooqpetproject.dto.CountryDto;
import com.company.jooqpetproject.exception.EntityNotFoundException;
import com.company.jooqpetproject.repository.CountryRepository;
import com.company.jooqpetproject.service.CityService;
import com.company.jooqpetproject.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;
    private final CityService cityService;

    @Override
    @Transactional
    public void insertCountry(CountryDto country) {
        log.info("Inserting country: {}", country);
        Long countyId = repository.insert(country);
        country.getCities().forEach(cityDto -> {
            cityDto.setCountryId(countyId);
            cityService.insertCity(cityDto);
        });
        log.info("Inserted country: {}", countyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryDto> getCountries() {
        log.info("Getting all countries");
        List<CountryDto> countries = repository.findAll();
        log.info("Got {} countries", countries.size());
        return countries;
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDto getCountry(Long id) {
        log.info("Getting country by id: {}", id);
        CountryDto country = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Country is not found"));
        List<CityDto> cities = cityService.getCitiesByCountryId(id);
        country.setCities(cities);
        log.info("Found country by id: {}", id);
        return country;
    }

    @Override
    @Transactional
    public void updateCountry(CountryDto countryDto) {
        log.info("Update country: {}", countryDto);
        CountryDto countryFromDB = repository.findByName(countryDto.getName()).orElseThrow(() -> new EntityNotFoundException("Country is not found"));
        countryDto.setId(countryFromDB.getId());
        Boolean updateResult = repository.update(countryDto);
        if (updateResult) {
            countryDto.getCities().forEach(cityDto -> {
                cityDto.setCountryId(countryDto.getId());
                cityService.insertCity(cityDto);
            });
        }
        log.info("Updated country: {}", countryDto.getId());
    }

    @Override
    @Transactional
    public void deleteCountry(Long id) {
        log.info("Deleting country by id: {}", id);
        cityService.deleteCityByCountryId(id);
        Boolean deleteResult = repository.delete(id);
        if (!deleteResult) {
            log.error("Not found country by id: {}", id);
            throw new EntityNotFoundException("Not found country by id " + id);
        }
        log.info("Deleted country: {}", id);
    }

}
