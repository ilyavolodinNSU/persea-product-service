package ru.persea.productservice.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.product.product.response.ProductBooleanFactorResponse;
import ru.persea.productservice.dto.product.product.response.ProductEnumFactorResponse;
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
        double numericScore = 0.7 * safetyScore + 0.3 * mineralScore;
        int metadataImpact = calculateBooleanImpact(productId) + calculateEnumImpact(productId);

        return clamp((int) Math.round(numericScore + metadataImpact));
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

    private int calculateBooleanImpact(Long productId) {
        return booleanFactorRepository.findAllWithRules(productId).stream()
            .mapToInt(this::resolveBooleanImpact)
            .sum();
    }

    private int resolveBooleanImpact(ProductBooleanFactorResponse factor) {
        if (factor.value() == null || factor.impact() == null || factor.impact() == 0) {
            return 0;
        }

        boolean value = Boolean.TRUE.equals(factor.value());
        int impact = factor.impact();

        if (impact > 0) {
            return value ? impact : 0;
        }

        // In the current dataset a negative "Наличие ..." impact means an absence penalty,
        // while a negative harmful-condition factor means a penalty when the value is true.
        if (isAbsencePenalty(factor.factorName())) {
            return value ? 0 : impact;
        }

        return value ? impact : 0;
    }

    private boolean isAbsencePenalty(String factorName) {
        if (factorName == null) return false;

        String normalizedName = factorName.toLowerCase(Locale.ROOT);
        return normalizedName.startsWith("наличие")
            || normalizedName.contains("отчёт")
            || normalizedName.contains("отчет")
            || normalizedName.contains("деклараци")
            || normalizedName.contains("сертификат");
    }

    private int calculateEnumImpact(Long productId) {
        return enumFactorRepository.findAllWithRules(productId).stream()
            .map(ProductEnumFactorResponse::impact)
            .filter(impact -> impact != null)
            .mapToInt(Integer::intValue)
            .sum();
    }

    private int clamp(int score) {
        return Math.max(0, Math.min(100, score));
    }
}
