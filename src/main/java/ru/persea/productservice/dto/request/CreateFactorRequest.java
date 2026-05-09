package ru.persea.productservice.dto.request;

public record CreateFactorRequest (
    String name,
    Integer typeId,
    String description
) {}
