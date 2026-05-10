package ru.persea.productservice.dto.product.request;

public record ProductEnumFactorRequest (
    Long factorId,
    Long enumValueId
) {} 
