package ru.persea.productservice.dto.factor.request;

public record CreateFactorEnumValueRequest (
    Long factorId,
    String value
) {}
