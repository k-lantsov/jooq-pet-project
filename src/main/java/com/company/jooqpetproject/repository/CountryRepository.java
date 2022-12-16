package com.company.jooqpetproject.repository;

import com.company.jooqpetproject.dto.CountryDto;
import com.company.jooqpetproject.exception.DataQueryException;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.company.jooqpetproject.domain.tables.City.CITY;
import static com.company.jooqpetproject.domain.tables.Country.COUNTRY;

@Repository
@Slf4j
public class CountryRepository {

    private final JdbcMapper<CountryDto> jdbcMapper;
    private final DSLContext context;

    @Autowired
    public CountryRepository(DSLContext context) {
        this.jdbcMapper = JdbcMapperFactory.newInstance()
                .addKeys("id", "country_id")
                .newMapper(CountryDto.class);
        this.context = context;
    }

    public Long insert(CountryDto countryDto) {
        return context.insertInto(COUNTRY, COUNTRY.NAME, COUNTRY.GOVERNMENT_FORM, COUNTRY.POPULATION)
                .values(countryDto.getName(), countryDto.getGovernmentForm(), countryDto.getPopulation())
                .returning(COUNTRY.ID)
                .fetchOptional()
                .orElseThrow(() -> new DataIntegrityViolationException("Error inserting entity"))
                .get(COUNTRY.ID);
    }

    public List<CountryDto> findAll() {
        ResultSet resultSet = context.select(COUNTRY.NAME, COUNTRY.GOVERNMENT_FORM, COUNTRY.POPULATION)
                .from(COUNTRY)
                .fetchResultSet();
        return transformResultSetIntoList(resultSet);
    }

    public Optional<CountryDto> findById(Long id) {
        ResultSet resultSet = context.select(COUNTRY.NAME, COUNTRY.GOVERNMENT_FORM, COUNTRY.POPULATION)
                .from(COUNTRY)
                .where(COUNTRY.ID.eq(id))
                .fetchResultSet();
        return transformResultSetIntoObject(resultSet);
    }

    public Optional<CountryDto> findByName(String name) {
        ResultSet resultSet = context.select(COUNTRY.ID, COUNTRY.NAME, COUNTRY.GOVERNMENT_FORM, COUNTRY.POPULATION)
                .from(COUNTRY)
                .where(COUNTRY.NAME.eq(name))
                .fetchResultSet();
        return transformResultSetIntoObject(resultSet);
    }

    public Boolean update(CountryDto countryDto) {
        return context.update(COUNTRY)
                .set(COUNTRY.NAME, countryDto.getName())
                .set(COUNTRY.GOVERNMENT_FORM, countryDto.getGovernmentForm())
                .set(COUNTRY.POPULATION, countryDto.getPopulation())
                .where(COUNTRY.ID.eq(countryDto.getId()))
                .execute() == 1;
    }

    public Boolean delete(Long id) {
        return context.deleteFrom(COUNTRY)
                .where(COUNTRY.ID.eq(id))
                .execute() == 1;
    }

    private List<CountryDto> transformResultSetIntoList(ResultSet resultSet) {
        try {
            return jdbcMapper.stream(resultSet).collect(Collectors.toList());
        } catch (SQLException e) {
            log.error("Cannot transform query result into a list because an error occurred", e);
            throw new DataQueryException("Cannot transform query result into a list because an error occurred", e);
        }
    }

    private Optional<CountryDto> transformResultSetIntoObject(ResultSet resultSet) {
        try {
            Iterator<CountryDto> iterator = jdbcMapper.iterator(resultSet);
            if (!iterator.hasNext()) {
                return Optional.empty();
            }
            CountryDto found = iterator.next();

            if (iterator.hasNext()) {
                throw new DataQueryException("Cannot transform into an object because multiple countries were found");
            }
            return Optional.of(found);
        } catch (SQLException e) {
            log.error("Error took place", e);
            throw new DataQueryException("Cannot transform query result into a list because an error occurred", e);
        }
    }
}
