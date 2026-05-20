package ru.persea.productservice.dto.factor.request;

public record CreateFactorRequest (
    String name,
    Short typeId,
    String description
) {}
