package ru.persea.productservice.dto.product.product.request;

public record ProductBooleanFactorRequest (
    Long factorId,
    Boolean value
) {}
