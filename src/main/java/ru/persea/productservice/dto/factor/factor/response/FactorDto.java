package ru.persea.productservice.dto.factor.response;

import ru.persea.productservice.dto.factorType.response.FactorTypeDto;

public record FactorDto(
    String name,
    FactorTypeDto type,
    String description
) {}
