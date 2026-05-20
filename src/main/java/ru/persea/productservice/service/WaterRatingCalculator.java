package ru.persea.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.product.product.response.ProductNumericFactorResponse;
import ru.persea.productservice.repository.product.ProductBooleanFactorRepository;
import ru.persea.productservice.repository.product.ProductEnumFactorRepository;
import ru.persea.productservice.repository.product.ProductNumericFactorRepository;

@Service
@RequiredArgsConstructor
public class WaterRatingCalculator implements RatingCalculator {
    private final static String WATER_CATEGORY_CODE = "WATER";

    private final ProductNumericFactorRepository numericFactorRepository;
    private final ProductBooleanFactorRepository booleanFactorRepository;
    private final ProductEnumFactorRepository enumFactorRepository;
    
    @Override
    public boolean support(String categoryCode) {
        return WATER_CATEGORY_CODE.equals(categoryCode);
    }

    @Override
    public int calculate(Long productId) {
        var numFactors = numericFactorRepository.findAllWithRules(productId);

        var safetyFactors = numFactors.stream()
            .filter(f -> f.minValue() == 0.0).toList();
        var mineralFactors = numFactors.stream()
            .filter(f -> f.minValue() > 0.0).toList();

        double safetyScore  = calculateDuaWqi(safetyFactors);
        double mineralScore = calculateDuaWqi(mineralFactors);

        // 70% безопасность, 30% физиологическая полноценность
        double finalScore = 0.7 * safetyScore + 0.3 * mineralScore;

        return (int) Math.round(finalScore);
    }

    private double calculateDuaWqi(List<ProductNumericFactorResponse> factors) {
        if (factors.isEmpty()) return 100.0;

        var SIs = factors.stream()
            .map(f -> {
                double idealValue = f.minValue() == 0.0
                    ? 0.0
                    : f.minValue() + (f.maxValue() - f.minValue()) / 2.0;

                double maxDeviation = f.minValue() == 0.0
                    ? f.maxValue()
                    : (f.maxValue() - f.minValue()) / 2.0;

                if (maxDeviation == 0.0) return 0.0;

                return Math.min(1.0, Math.abs(f.amount() - idealValue) / maxDeviation);
            })
            .toList();

        double sum = SIs.stream().mapToDouble(si -> Math.exp(si) - 1).sum();

        if (sum == 0.0) return 100.0;

        double penalty = 0.0;
        for (int i = 0; i < SIs.size(); i++) {
            double w = (Math.exp(SIs.get(i)) - 1) / sum;
            penalty += w * SIs.get(i) * 100;
        }

        return 100.0 - penalty;
    }
}
