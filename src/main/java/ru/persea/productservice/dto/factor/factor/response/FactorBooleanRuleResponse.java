package ru.persea.productservice.dto.factor.factor.response;

import ru.persea.productservice.dto.product.category.response.CategoryDto;

public record FactorBooleanRuleResponse (
    Long id,
    FactorDto factor,
    CategoryDto category,
    Integer impact
) {}
