package ru.persea.productservice.dto.factor.response;

import ru.persea.productservice.dto.factor.FactorDto;
import ru.persea.productservice.dto.product.CategoryDto;

public record FactorBooleanRuleResponse (
    Long id,
    FactorDto factor,
    CategoryDto category,
    Integer impact
) {}
