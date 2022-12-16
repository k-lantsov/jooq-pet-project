package com.company.jooqpetproject.service.impl;

import com.company.jooqpetproject.dto.CityDto;
import com.company.jooqpetproject.repository.CityRepository;
import com.company.jooqpetproject.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService {

    private final CityRepository repository;

    @Override
    @Transactional
    public void insertCity(CityDto cityDto) {
        log.info("Inserting city: {}", cityDto);
        Boolean getResult = getCityByNameAndCountryId(cityDto.getName(), cityDto.getCountryId());
        if (!getResult) {
            Long cityId = repository.insert(cityDto);
            log.info("Inserted city: {}", cityId);
            return;
        }
        log.info("The city has already inserted");
    }

    @Override
    @Transactional
    public Boolean getCityByNameAndCountryId(String name, Long countryId) {
        log.info("Getting city by name {} and countryId {}", name, countryId);
        Boolean findResult = repository.findByNameAndCountryId(name, countryId);
        if (findResult) {
            log.info("Got city by name {} and countryId {}", name, countryId);
            return true;
        }
        log.info("Not found city by name {} and countryId {}", name, countryId);
        return false;
    }

    @Override
    @Transactional
    public Boolean deleteCityById(Long id) {
        log.info("Deleting city by id: {}", id);
        Boolean deleteResult = repository.deleteById(id);
        if (deleteResult) {
            log.info("city was deleted");
        }
        return deleteResult;
    }

    @Override
    @Transactional
    public void deleteCityByCountryId(Long countryId) {
        log.info("Deleting cities by countryId: {}", countryId);
        Integer deleteResult = repository.deleteByCountryId(countryId);
        log.info("There were {} deleting cities", deleteResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityDto> getCitiesByCountryId(Long countryId) {
        log.info("Getting cities by countryId: {}", countryId);
        List<CityDto> cities = repository.findCitiesByCountryId(countryId);
        log.info("There were {} founded cities by countryId", cities.size());
        return cities;
    }
}
