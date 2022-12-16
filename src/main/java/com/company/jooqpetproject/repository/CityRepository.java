package com.company.jooqpetproject.repository;

import com.company.jooqpetproject.dto.CityDto;
import com.company.jooqpetproject.dto.CountryDto;
import com.company.jooqpetproject.exception.DataQueryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.jooqpetproject.domain.tables.City.CITY;

@Repository
@Slf4j
public class CityRepository {

    private final DSLContext context;
    private final JdbcMapper<CityDto> jdbcMapper;

    @Autowired
    public CityRepository(DSLContext context) {
        this.context = context;
        this.jdbcMapper = JdbcMapperFactory.newInstance()
                .addKeys("id", "country_id")
                .newMapper(CityDto.class);
    }

    public Long insert(CityDto cityDto) {
        return context.insertInto(CITY, CITY.NAME, CITY.COUNTRY_ID)
                .values(cityDto.getName(), cityDto.getCountryId())
                .returning(CITY.ID)
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("Error inserting entity"))
                .get(CITY.ID);
    }

    public Boolean findByNameAndCountryId(String name, Long countryId) {
        return context.selectFrom(CITY)
                .where(CITY.NAME.eq(name))
                .and(CITY.COUNTRY_ID.eq(countryId))
                .fetch().isNotEmpty();
    }

    public Boolean deleteById(Long id) {
        return context.deleteFrom(CITY)
                .where(CITY.ID.eq(id))
                .execute() == 1;
    }

    public Integer deleteByCountryId(Long id) {
        return context.deleteFrom(CITY)
                .where(CITY.COUNTRY_ID.eq(id))
                .execute();
    }

    public List<CityDto> findCitiesByCountryId(Long countryId) {
        ResultSet resultSet = context.select(CITY.NAME)
                .from(CITY)
                .where(CITY.COUNTRY_ID.eq(countryId))
                .fetchResultSet();
        return transformResultSetIntoList(resultSet);
    }

    private List<CityDto> transformResultSetIntoList(ResultSet resultSet) {
        try {
            return jdbcMapper.stream(resultSet).collect(Collectors.toList());
        } catch (SQLException e) {
            log.error("Cannot transform query result into a list because an error occurred", e);
            throw new DataQueryException("Cannot transform query result into a list because an error occurred", e);
        }
    }
}
