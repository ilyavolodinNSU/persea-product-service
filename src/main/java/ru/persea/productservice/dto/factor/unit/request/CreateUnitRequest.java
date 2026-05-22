package ru.persea.productservice.dto.factor.unit.request;

import jakarta.validation.constraints.NotBlank;

public record CreateUnitRequest(
    @NotBlank String name
) {}
