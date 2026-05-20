package ru.persea.productservice.dto.factor.response;

public record FactorEnumValueResponse (
    Long id,
    FactorDto factor,
    String value
) {}
