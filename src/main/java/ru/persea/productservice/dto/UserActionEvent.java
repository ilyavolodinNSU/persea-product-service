package ru.persea.productservice.dto;

import java.util.UUID;

public record UserActionEvent(
    UUID userId,
    String action
) {}
