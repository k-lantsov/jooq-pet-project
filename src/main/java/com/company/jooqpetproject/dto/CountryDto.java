package com.company.jooqpetproject.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CountryDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String governmentForm;

    @NotNull
    private int population;

    private List<CityDto> cities;
}
