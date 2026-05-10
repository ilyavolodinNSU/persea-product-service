package ru.persea.productservice.dto.product.response;

import ru.persea.productservice.dto.product.BrandDto;
import ru.persea.productservice.dto.product.CategoryDto;

public record ProductItemResponse(
    Long id,
    String name,
    BrandDto brand,
    CategoryDto category,
    Integer rating,
    String imageUri
) {}
