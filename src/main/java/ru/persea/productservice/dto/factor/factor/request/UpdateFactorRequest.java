package ru.persea.productservice.dto.factor.factor.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateFactorRequest(
    @NotBlank String name,
    @NotNull Short typeId,
    String description
) {}
