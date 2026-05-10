package ru.persea.productservice.dto.factor;

public record FactorDto(
    String name,
    FactorTypeDto type,
    String description
) {}
