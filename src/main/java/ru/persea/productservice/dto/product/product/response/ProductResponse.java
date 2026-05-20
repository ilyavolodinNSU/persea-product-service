package ru.persea.productservice.dto.product.product.response;

import java.util.List;

import ru.persea.productservice.dto.product.brand.response.BrandDto;
import ru.persea.productservice.dto.product.category.response.CategoryDto;

public record ProductResponse(
    Long id,
    String name, 
    BrandDto brand,
    CategoryDto category,
    Integer rating,
    String imageURI,
    List<ProductNumericFactorResponse> numericFactors,
    List<ProductBooleanFactorResponse> booleanFactors,
    List<ProductEnumFactorResponse> enumFactors
) {}
