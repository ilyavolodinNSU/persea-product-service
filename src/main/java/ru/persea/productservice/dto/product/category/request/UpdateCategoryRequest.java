package ru.persea.productservice.dto.product.category.request;

public record UpdateCategoryRequest(
    String name,
    String code
) {}
