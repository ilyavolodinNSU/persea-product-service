package ru.persea.productservice.dto.product.product.response;

public record ProductBooleanFactorResponse (
    Long id,
    Long factorId,
    String factorName,
    Boolean value,
    Integer impact
) {}
