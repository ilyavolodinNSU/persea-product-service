package ru.persea.productservice.dto.factor.request;

public record CreateFactorBooleanRuleRequest (
    Long factorId,
    Long categoryId,
    Integer impact
) {}
