package ru.persea.productservice.dto.factor.response;

import ru.persea.productservice.dto.category.response.CategoryDto;

public record FactorBooleanRuleResponse (
    Long id,
    FactorDto factor,
    CategoryDto category,
    Integer impact
) {}
