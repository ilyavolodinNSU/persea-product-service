package ru.persea.productservice.dto.product.product;

public record ProductSearchDto(
    Long id,
    String name, 
    Integer rating,
    String imageURI
) {}
