package ru.persea.productservice.dto.product.response;

public record ProductEnumFactorResponse (
    Long id,
    Long factorId,
    String factorName,
    String enumValue,
    Integer impact
) {} 
