package ru.persea.productservice.dto.product.product;

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
