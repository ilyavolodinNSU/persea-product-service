package ru.persea.productservice.dto.product.request;

public record ProductBooleanFactorRequest (
    Long factorId,
    Boolean value
) {}
