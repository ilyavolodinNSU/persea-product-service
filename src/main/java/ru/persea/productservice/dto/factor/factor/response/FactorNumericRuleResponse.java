package ru.persea.productservice.dto.factor.response;

import ru.persea.productservice.dto.unit.response.UnitDto;
import ru.persea.productservice.dto.category.response.CategoryDto;

public record FactorNumericRuleResponse (
    Long id,
    FactorDto factor,
    CategoryDto category,
    UnitDto unit,
    Double minValue,
    Double maxValue
) {}
