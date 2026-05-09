package ru.persea.productservice.dto.request;

public record CreateNumericFactorRuleRequest (
    Integer factorId,
    Integer categoryId,
    Integer unitId,
    Integer minValue,
    Integer maxValue
) {}
