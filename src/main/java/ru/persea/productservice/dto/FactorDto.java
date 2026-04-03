package ru.persea.productservice.dto;

public record FactorDto(
    String name,
    String type,
    String unit,
    Double amount
) {}
