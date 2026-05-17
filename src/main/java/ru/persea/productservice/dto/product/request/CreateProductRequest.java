package ru.persea.productservice.dto.product.request;

import java.util.List;


public record CreateProductRequest(
    String name, 
    Long categoryId,
    Long brandId,
    String imageURI,
    List<ProductNumericFactorRequest> numericFactors,
    List<ProductBooleanFactorRequest> booleanFactors,
    List<ProductEnumFactorRequest> enumFactors
) {}
