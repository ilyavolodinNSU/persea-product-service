package ru.persea.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductInclude {
    FACTORS("factors"),
    DESCRIPTION("description"),
    REVIEWS("reviews");

    private final String param;
}
