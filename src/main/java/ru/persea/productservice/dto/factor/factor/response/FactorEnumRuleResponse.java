package ru.persea.productservice.dto.factor.factor.response;

import ru.persea.productservice.dto.product.category.response.CategoryDto;

public record FactorEnumRuleResponse (
    Long id,
    CategoryDto category,
    FactorEnumValueResponse enumValue,
    Integer impact
) {}
