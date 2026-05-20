package ru.persea.productservice.dto.factor.factor.request;

import jakarta.validation.constraints.NotNull;

public record UpdateFactorEnumRuleRequest(
    @NotNull Long categoryId,
    @NotNull Long valueId,
    @NotNull Integer impact
) {}
