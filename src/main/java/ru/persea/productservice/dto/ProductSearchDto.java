package ru.persea.productservice.dto;

public record ProductSearchDto(
    Long id,
    String name, 
    Integer rating,
    String imageURI
) {}
