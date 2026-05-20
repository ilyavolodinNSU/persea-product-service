package ru.persea.productservice.dto.factor.factor.response;

import ru.persea.productservice.dto.factor.unit.response.UnitDto;
import ru.persea.productservice.dto.product.category.response.CategoryDto;

public record FactorNumericRuleResponse (
    Long id,
    FactorDto factor,
    CategoryDto category,
    UnitDto unit,
    Double minValue,
    Double maxValue
) {}
