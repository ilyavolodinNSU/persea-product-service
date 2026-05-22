package ru.persea.productservice.dto.factor.factor.request;

public record CreateFactorRequest (
    String name,
    Short typeId,
    String description
) {}
