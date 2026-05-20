package ru.persea.productservice.dto.product.category.response;

public record CategoryDto (
    Long id,
    String name,
    String code
) {}
