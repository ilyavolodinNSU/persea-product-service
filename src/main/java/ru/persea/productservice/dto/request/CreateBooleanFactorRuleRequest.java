package ru.persea.productservice.dto.request;

public record CreateBooleanFactorRuleRequest (
    Integer factorId,
    Integer categoryId,
    Integer impact
) {}
