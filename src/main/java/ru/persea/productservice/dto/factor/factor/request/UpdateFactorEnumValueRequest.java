package ru.persea.productservice.dto.factor.factor.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateFactorEnumValueRequest(
    @NotBlank String value
) {}
