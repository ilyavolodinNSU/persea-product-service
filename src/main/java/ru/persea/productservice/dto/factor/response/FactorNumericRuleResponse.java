package ru.persea.productservice.dto.factor.response;

import ru.persea.productservice.dto.factor.FactorDto;
import ru.persea.productservice.dto.factor.UnitDto;
import ru.persea.productservice.dto.product.CategoryDto;

public record FactorNumericRuleResponse (
    Long id,
    FactorDto factor,
    CategoryDto category,
    UnitDto unit,
    Double minValue,
    Double maxValue
) {}
