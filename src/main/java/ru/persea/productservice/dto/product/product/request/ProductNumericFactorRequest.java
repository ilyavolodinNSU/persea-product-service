package ru.persea.productservice.dto.product.product.request;

public record ProductNumericFactorRequest (
    Long factorId,
    Double amount
) {} 
