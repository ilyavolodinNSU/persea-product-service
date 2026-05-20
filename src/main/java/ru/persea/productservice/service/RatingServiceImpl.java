package ru.persea.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.repository.product.ProductsRepository;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final ProductsRepository productsRepository;

    private final List<RatingCalculator> calculators;

    @Override
    public int calculate(Long productId) {
        var code = productsRepository.findById(productId).orElseThrow().getCategory().getCode();

        return calculators.stream()
            .filter(c -> c.support(code))
            .findFirst()
            .orElseThrow()
            .calculate(productId);
    }
}
