package ru.persea.productservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.persea.productservice.dto.product.product.response.ProductNumericFactorResponse;
import ru.persea.productservice.repository.product.ProductBooleanFactorRepository;
import ru.persea.productservice.repository.product.ProductEnumFactorRepository;
import ru.persea.productservice.repository.product.ProductNumericFactorRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaterRatingCalculatorTest {

    @Mock
    private ProductNumericFactorRepository numericFactorRepository;
    @Mock
    private ProductBooleanFactorRepository booleanFactorRepository;
    @Mock
    private ProductEnumFactorRepository enumFactorRepository;

    @InjectMocks
    private WaterRatingCalculator calculator;

    @Test
    void support_shouldReturnTrueForWaterCategory() {
        assertThat(calculator.support("WATER")).isTrue();
        assertThat(calculator.support("water")).isFalse();
        assertThat(calculator.support("OTHER")).isFalse();
    }

    @Test
    void calculate_noFactors_shouldReturn100() {
        when(numericFactorRepository.findAllWithRules(1L)).thenReturn(Collections.emptyList());

        int result = calculator.calculate(1L);
        assertThat(result).isEqualTo(100);
        verify(numericFactorRepository).findAllWithRules(1L);
    }

    @Test
    void calculate_singleSafetyFactor_shouldReturnExpectedScore() {
        // min=0, max=10, amount=5 => SI=0.5, safetyScore=50, mineralScore=100 => final=0.7*50+0.3*100=65
        var factors = List.of(
                new ProductNumericFactorResponse(1L, 1L, "Lead", "mg/L", 5.0, 0.0, 10.0)
        );
        when(numericFactorRepository.findAllWithRules(2L)).thenReturn(factors);

        int score = calculator.calculate(2L);
        assertThat(score).isEqualTo(65);
    }

    @Test
    void calculate_singleMineralFactor_atIdealValue_shouldReturn100() {
        // min=2, max=8, amount=5 (идеальное значение) => SI=0, mineralScore=100, safetyScore=100 => 100
        var factors = List.of(
                new ProductNumericFactorResponse(2L, 2L, "Calcium", "mg/L", 5.0, 2.0, 8.0)
        );
        when(numericFactorRepository.findAllWithRules(3L)).thenReturn(factors);

        int score = calculator.calculate(3L);
        assertThat(score).isEqualTo(100);
    }

    @Test
    void calculate_mixedFactors_shouldComputeWeightedScore() {
        // Два safety-фактора и один mineral
        List<ProductNumericFactorResponse> factors = List.of(
                new ProductNumericFactorResponse(1L, 1L, "S1", "u", 2.0, 0.0, 10.0), // SI=0.2
                new ProductNumericFactorResponse(2L, 2L, "S2", "u", 8.0, 0.0, 10.0), // SI=0.8
                new ProductNumericFactorResponse(3L, 3L, "M1", "u", 5.0, 2.0, 8.0)   // SI=0
        );
        when(numericFactorRepository.findAllWithRules(4L)).thenReturn(factors);

        int score = calculator.calculate(4L);

        // Ручной расчёт:
        // safety: SI1=0.2 (e^0.2-1≈0.2214), SI2=0.8 (e^0.8-1≈1.2255)
        // sum = 1.4469, w1=0.153, w2=0.847
        // penalty = 0.153*0.2*100 + 0.847*0.8*100 = 3.06 + 67.76 = 70.82
        // safetyScore = 29.18
        // mineralScore = 100 (SI=0)
        // final = 0.7*29.18 + 0.3*100 = 20.426 + 30 = 50.426 -> 50
        assertThat(score).isEqualTo(50);
    }

    @Test
    void calculate_allFactorsPerfect_shouldReturn100() {
        // Все amount = idealValue -> все SI=0 -> safetyScore=100, mineralScore=100
        List<ProductNumericFactorResponse> factors = List.of(
                new ProductNumericFactorResponse(1L, 1L, "S", "u", 0.0, 0.0, 10.0),   // ideal=0, amount=0 => SI=0
                new ProductNumericFactorResponse(2L, 2L, "M", "u", 5.0, 2.0, 8.0)     // ideal=5, amount=5 => SI=0
        );
        when(numericFactorRepository.findAllWithRules(5L)).thenReturn(factors);

        int score = calculator.calculate(5L);
        assertThat(score).isEqualTo(100);
    }

    @Test
    void calculate_maxDeviationZero_shouldHandleGracefully() {
        // min=max=5, amount=5 => ideal=5? Для safety min=0 -> формула иначе.
        // Но для mineral min>0: ideal = 5 + (5-5)/2 =5, maxDeviation=(5-5)/2=0
        // В коде: if (maxDeviation == 0.0) return 0.0; SI=0
        List<ProductNumericFactorResponse> factors = List.of(
                new ProductNumericFactorResponse(3L, 3L, "M", "u", 5.0, 5.0, 5.0)
        );
        when(numericFactorRepository.findAllWithRules(6L)).thenReturn(factors);

        int score = calculator.calculate(6L);
        assertThat(score).isEqualTo(100); // mineralScore=100, safety=100
    }

    @Test
    void calculate_onlySafetyFactors_shouldReturnWeightedScore() {
        // Только safety, mineral пуст -> mineralScore=100
        List<ProductNumericFactorResponse> factors = List.of(
                new ProductNumericFactorResponse(1L, 1L, "S", "u", 10.0, 0.0, 10.0) // amount=max => SI=1.0
        );
        when(numericFactorRepository.findAllWithRules(7L)).thenReturn(factors);

        // SI=1, exp(1)-1≈1.7183, sum=1.7183, w=1, penalty=1*1*100=100, safetyScore=0
        // final = 0.7*0 + 0.3*100 = 30
        int score = calculator.calculate(7L);
        assertThat(score).isEqualTo(30);
    }
}