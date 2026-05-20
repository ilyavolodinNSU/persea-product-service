package ru.persea.productservice.dto.product.product.response;

public record ProductNumericFactorResponse (
    Long id,
    Long factorId,
    String factorName,
    String unitName,
    Double amount,
    Double minValue,
    Double maxValue
) {} 
