package ru.persea.productservice.dto.product.product.request;

import java.util.List;


public record CreateProductRequest(
    String name, 
    Long categoryId,
    Long brandId,
    String imageURI,
    String barcode,
    List<ProductNumericFactorRequest> numericFactors,
    List<ProductBooleanFactorRequest> booleanFactors,
    List<ProductEnumFactorRequest> enumFactors
) {}
