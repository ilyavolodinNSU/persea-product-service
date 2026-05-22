package ru.persea.productservice.dto.product.product.response;

import ru.persea.productservice.dto.product.brand.response.BrandDto;
import ru.persea.productservice.dto.product.category.response.CategoryDto;

public record ProductItemResponse(
    Long id,
    String name,
    BrandDto brand,
    CategoryDto category,
    Integer rating,
    String imageUri
) {}
