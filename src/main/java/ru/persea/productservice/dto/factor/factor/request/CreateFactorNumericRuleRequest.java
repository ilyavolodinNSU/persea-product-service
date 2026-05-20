package ru.persea.productservice.dto.factor.request;

public record CreateFactorNumericRuleRequest (
    Long categoryId,
    Short unitId,
    Double minValue,
    Double maxValue
) {}
