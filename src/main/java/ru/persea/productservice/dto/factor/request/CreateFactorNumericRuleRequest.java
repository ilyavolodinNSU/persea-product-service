package ru.persea.productservice.dto.factor.request;

public record CreateFactorNumericRuleRequest (
    Long factorId,
    Long categoryId,
    Short unitId,
    Double minValue,
    Double maxValue
) {}
