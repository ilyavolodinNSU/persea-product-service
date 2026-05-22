package ru.persea.productservice.dto.factor.factorType.request;

import jakarta.validation.constraints.NotBlank;

public record CreateFactorTypeRequest(
    @NotBlank String name
) {}
