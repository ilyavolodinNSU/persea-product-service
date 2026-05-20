package ru.persea.productservice.dto.factor.factor.request;

public record CreateFactorBooleanRuleRequest (
    Long categoryId,
    Integer impact
) {}
