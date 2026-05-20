package ru.persea.productservice.dto.factor.factor.request;

import jakarta.validation.constraints.NotNull;

public record UpdateFactorBooleanRuleRequest(
    @NotNull Long categoryId,
    @NotNull Integer impact
) {}
