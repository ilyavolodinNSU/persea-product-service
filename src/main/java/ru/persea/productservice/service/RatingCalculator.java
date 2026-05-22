package ru.persea.productservice.service;

public interface RatingCalculator {
    public boolean support(String categoryCode);

    public int calculate(Long productId);
}
