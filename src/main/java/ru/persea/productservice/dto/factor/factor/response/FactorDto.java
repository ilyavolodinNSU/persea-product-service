package ru.persea.productservice.dto.factor.factor.response;

import ru.persea.productservice.dto.factor.factorType.response.FactorTypeDto;

public record FactorDto(
    String name,
    FactorTypeDto type,
    String description
) {}
