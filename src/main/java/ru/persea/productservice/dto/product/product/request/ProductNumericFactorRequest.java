package ru.persea.productservice.dto.product.request;

public record ProductNumericFactorRequest (
    Long factorId,
    Double amount
) {} 
