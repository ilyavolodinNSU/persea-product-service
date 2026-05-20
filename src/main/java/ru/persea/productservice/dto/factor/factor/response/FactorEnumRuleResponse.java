package ru.persea.productservice.dto.factor.response;

import ru.persea.productservice.dto.category.response.CategoryDto;

public record FactorEnumRuleResponse (
    Long id,
    CategoryDto category,
    FactorEnumValueResponse enumValue,
    Integer impact
) {}
