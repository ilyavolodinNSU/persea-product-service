package ru.persea.productservice.dto.factor.request;

import java.util.List;

public record CreateFactorEnumRuleRequest (
    Long categoryId,
    Long valueId,
    Integer impact
) {}
