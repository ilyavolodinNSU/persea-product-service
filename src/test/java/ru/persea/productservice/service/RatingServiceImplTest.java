package ru.persea.productservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.persea.productservice.entity.product.CategoryEntity;
import ru.persea.productservice.entity.product.ProductEntity;
import ru.persea.productservice.repository.product.ProductsRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private RatingCalculator calculator1;

    @Mock
    private RatingCalculator calculator2;

    @Mock
    private List<RatingCalculator> calculators;  // мокаем сам список

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    void calculate_shouldUseSupportingCalculator() {
        Long productId = 1L;
        ProductEntity product = new ProductEntity();
        CategoryEntity category = new CategoryEntity(1L, "Вода", "WATER");
        product.setCategory(category);

        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));
        when(calculators.stream()).thenReturn(List.of(calculator1, calculator2).stream());
        when(calculator1.support("WATER")).thenReturn(true);
        when(calculator1.calculate(productId)).thenReturn(85);

        int result = ratingService.calculate(productId);

        assertThat(result).isEqualTo(85);
        verify(calculator1).calculate(productId);
        verify(calculator2, never()).calculate(anyLong());
    }

    @Test
    void calculate_shouldThrowIfNoCalculatorSupports() {
        Long productId = 2L;
        ProductEntity product = new ProductEntity();
        product.setCategory(new CategoryEntity(2L, "Другое", "OTHER"));

        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));
        when(calculators.stream()).thenReturn(List.of(calculator1).stream());
        when(calculator1.support("OTHER")).thenReturn(false);

        assertThrows(java.util.NoSuchElementException.class, () -> ratingService.calculate(productId));
    }

    @Test
    void calculate_shouldThrowIfProductNotFound() {
        when(productsRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(java.util.NoSuchElementException.class, () -> ratingService.calculate(99L));
    }
}