package ru.persea.productservice.dto.factor.request;

public record CreateFactorBooleanRuleRequest (
    Long categoryId,
    Integer impact
) {}
