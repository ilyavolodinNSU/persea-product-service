package ru.persea.productservice.dto.factor.factor.request;

public record CreateFactorNumericRuleRequest (
    Long categoryId,
    Short unitId,
    Double minValue,
    Double maxValue
) {}
