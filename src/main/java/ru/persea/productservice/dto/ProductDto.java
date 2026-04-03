package ru.persea.productservice.dto;

import java.util.List;

public record ProductDto(
    Long id,
    String name, 
    Integer rating,
    String imageURI,
    List<FactorDto> factors
) {}
