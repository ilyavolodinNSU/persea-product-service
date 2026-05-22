package ru.persea.productservice.dto.product.category.request;

public record CreateCategoryRequest(
    String name,
    String code
) {}
