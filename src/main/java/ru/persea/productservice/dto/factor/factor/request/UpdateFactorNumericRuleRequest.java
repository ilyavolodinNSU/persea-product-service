package ru.persea.productservice.dto.factor.factor.request;

import jakarta.validation.constraints.NotNull;

public record UpdateFactorNumericRuleRequest(
    @NotNull Long categoryId,
    @NotNull Short unitId,
    @NotNull Double minValue,
    @NotNull Double maxValue
) {}
