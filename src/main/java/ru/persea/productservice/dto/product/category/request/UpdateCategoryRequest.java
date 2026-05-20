package ru.persea.productservice.dto.category.request;

public record UpdateCategoryRequest(
    String name,
    String code
) {}
