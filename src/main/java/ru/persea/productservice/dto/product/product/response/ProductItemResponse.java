package ru.persea.productservice.dto.product.response;

import ru.persea.productservice.dto.brand.response.BrandDto;
import ru.persea.productservice.dto.category.response.CategoryDto;

public record ProductItemResponse(
    Long id,
    String name,
    BrandDto brand,
    CategoryDto category,
    Integer rating,
    String imageUri
) {}
