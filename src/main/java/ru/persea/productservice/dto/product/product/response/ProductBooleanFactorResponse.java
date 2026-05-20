package ru.persea.productservice.dto.product.response;

public record ProductBooleanFactorResponse (
    Long id,
    Long factorId,
    String factorName,
    Boolean value,
    Integer impact
) {}
