package ru.persea.productservice.dto.category.request;

public record CreateCategoryRequest(
    String name,
    String code
) {}
