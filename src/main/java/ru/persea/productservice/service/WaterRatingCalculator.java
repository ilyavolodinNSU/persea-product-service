package ru.persea.productservice.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.repository.ProductBooleanFactorRepository;
import ru.persea.productservice.repository.ProductEnumFactorRepository;
import ru.persea.productservice.repository.ProductNumericFactorRepository;

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

        // субиндексироание от 0 до 1
        var SIs = numFactors.stream()
            .map(f -> {
                Double idealValue = (f.minValue() == 0.0) 
                    ? 0.0 // чем меньше тем лучше
                    : f.minValue() + (f.maxValue()-f.minValue()) / 2.0; // серидина рекомендованного диапазона

                if (f.maxValue()-idealValue == 0.0) return 0.0;

                return Math.min(1.0, Math.abs((f.amount()-idealValue) / (f.maxValue()-idealValue)));
            })
            .toList();
        
        System.out.println("SIs:");
        SIs.forEach(System.out::println);

        // взвешивание
        Double sum = SIs.stream().mapToDouble(si -> Math.exp(si) - 1).sum();

        System.out.println("sum "+sum);

        var Ws = SIs.stream().map(SI -> (Math.exp(SI)-1)/sum).toList();

        System.out.println("Ws:");
        Ws.forEach(System.out::println);

        // штраф
        Double penalty = 0.0;

        for (int i = 0; i < Ws.size(); i++) penalty += Ws.get(i)*SIs.get(i)*100;

        System.out.println("penalty "+penalty);

        System.out.println("rating "+(int)(100.0-penalty));
        
        return (int) Math.round(100.0-penalty);
    }
}
