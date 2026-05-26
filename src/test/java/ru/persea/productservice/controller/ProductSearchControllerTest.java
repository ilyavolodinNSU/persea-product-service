package ru.persea.productservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.persea.productservice.dto.product.product.ProductSearchDto;
import ru.persea.productservice.service.ProductService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductSearchControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private ProductSearchController controller;

    @Test
    void searchProducts_shouldReturnOkAndCallService() {
        // given
        String query = "тест";
        Integer categoryId = 1;
        Set<Integer> brandIds = Set.of(10, 20);
        Integer minRating = 3;
        Integer maxRating = 5;

        List<ProductSearchDto> expected = List.of(
                new ProductSearchDto(1L, "Товар 1", 4, "url1"),
                new ProductSearchDto(2L, "Товар 2", 5, "url2")
        );
        when(productService.searchProducts(query, categoryId, brandIds, minRating, maxRating, pageable))
                .thenReturn(expected);

        // when
        ResponseEntity<List<ProductSearchDto>> response = controller.searchProducts(
                query, categoryId, brandIds, minRating, maxRating, pageable
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyElementsOf(expected);
        verify(productService).searchProducts(query, categoryId, brandIds, minRating, maxRating, pageable);
    }

    @Test
    void searchProducts_withNullParams_shouldCallServiceWithNulls() {
        // given
        List<ProductSearchDto> expected = Collections.emptyList();
        when(productService.searchProducts(null, null, null, null, null, pageable))
                .thenReturn(expected);

        // when
        ResponseEntity<List<ProductSearchDto>> response = controller.searchProducts(
                null, null, null, null, null, pageable
        );

        // then
        assertThat(response.getBody()).isEmpty();
        verify(productService).searchProducts(null, null, null, null, null, pageable);
    }

    @Test
    void searchProducts_withEmptyBrandSet_shouldCallServiceWithEmptySet() {
        Set<Integer> emptyBrands = Collections.emptySet();
        List<ProductSearchDto> expected = Collections.emptyList();
        when(productService.searchProducts("q", 1, emptyBrands, null, null, pageable))
                .thenReturn(expected);

        ResponseEntity<List<ProductSearchDto>> response = controller.searchProducts(
                "q", 1, emptyBrands, null, null, pageable
        );

        assertThat(response.getBody()).isEmpty();
        verify(productService).searchProducts("q", 1, emptyBrands, null, null, pageable);
    }

    @Test
    void getSuggestions_shouldReturnOkAndCallService() {
        String query = "тест";
        int limit = 10;
        List<String> expected = List.of("тест1", "тест2");
        when(productService.getSuggestions(query, limit)).thenReturn(expected);

        ResponseEntity<List<String>> response = controller.getSuggestions(query, limit);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyElementsOf(expected);
        verify(productService).getSuggestions(query, limit);
    }

    @Test
    void getSuggestions_withDefaultLimit_shouldCallServiceWithProvidedLimit() {
        // limit передается явно, даже если defaultValue в аннотации, контроллер получает уже разрешённое значение
        String query = "тест";
        int limit = 5; // допустим, клиент не передал, Spring подставит 5
        List<String> expected = List.of("тест");
        when(productService.getSuggestions(query, limit)).thenReturn(expected);

        ResponseEntity<List<String>> response = controller.getSuggestions(query, limit);

        assertThat(response.getBody()).containsExactly("тест");
        verify(productService).getSuggestions(query, limit);
    }
}