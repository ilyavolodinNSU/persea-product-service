package ru.persea.productservice.dto.factor.factor.request;

public record CreateFactorEnumRuleRequest (
    Long categoryId,
    Integer impact
) {}
