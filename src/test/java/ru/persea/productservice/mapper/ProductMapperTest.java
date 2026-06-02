package ru.persea.productservice.mapper;

import org.junit.jupiter.api.Test;
import ru.persea.productservice.dto.product.brand.response.BrandDto;
import ru.persea.productservice.dto.product.category.response.CategoryDto;
import ru.persea.productservice.dto.product.product.response.*;
import ru.persea.productservice.entity.product.BrandEntity;
import ru.persea.productservice.entity.product.CategoryEntity;
import ru.persea.productservice.entity.product.ProductEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapperImpl();

    @Test
    void toDto_CategoryEntity_ShouldMapCorrectly() {
        CategoryEntity entity = new CategoryEntity(1L, "Электроника", "electronics");
        CategoryDto dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Электроника");
        assertThat(dto.code()).isEqualTo("electronics");
    }

    @Test
    void toDto_BrandEntity_ShouldMapCorrectly() {
        BrandEntity entity = new BrandEntity(2L, "Samsung");
        BrandDto dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(2L);
        assertThat(dto.name()).isEqualTo("Samsung");
    }

    @Test
    void toDto_ProductEntity_ShouldMapCorrectly() {
        CategoryEntity category = new CategoryEntity(10L, "Телефоны", "phones");
        BrandEntity brand = new BrandEntity(20L, "Apple");
        ProductEntity entity = new ProductEntity();
        entity.setId(100L);
        entity.setName("iPhone 15");
        entity.setCategory(category);
        entity.setBrand(brand);
        entity.setRating(5);
        entity.setImageURI("http://image.url");
        entity.setBarcode("4600000000011");

        ProductResponse dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(100L);
        assertThat(dto.name()).isEqualTo("iPhone 15");
        assertThat(dto.category().id()).isEqualTo(10L);
        assertThat(dto.category().name()).isEqualTo("Телефоны");
        assertThat(dto.brand().id()).isEqualTo(20L);
        assertThat(dto.brand().name()).isEqualTo("Apple");
        assertThat(dto.rating()).isEqualTo(5);
        assertThat(dto.imageURI()).isEqualTo("http://image.url");
        assertThat(dto.barcode()).isEqualTo("4600000000011");
        // Проверяем, что списки факторов пустые, т.к. маппер не заполняет их из ProductEntity
        assertThat(dto.numericFactors()).isNull();
        assertThat(dto.booleanFactors()).isNull();
        assertThat(dto.enumFactors()).isNull();
    }

    @Test
    void toDto_ProductEntityWithFactorLists_ShouldMapAllFields() {
        CategoryEntity category = new CategoryEntity(1L, "Кат", "code");
        BrandEntity brand = new BrandEntity(2L, "Бренд");
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setName("Товар");
        entity.setCategory(category);
        entity.setBrand(brand);
        entity.setRating(3);
        entity.setImageURI("img");
        entity.setBarcode("4600000000028");

        List<ProductNumericFactorResponse> numFactors = List.of(
                new ProductNumericFactorResponse(1L, 10L, "Вес", "кг", 5.0, 0.0, 10.0)
        );
        List<ProductBooleanFactorResponse> boolFactors = List.of(
                new ProductBooleanFactorResponse(2L, 20L, "Складной", true, 1)
        );
        List<ProductEnumFactorResponse> enumFactors = List.of(
                new ProductEnumFactorResponse(3L, 30L, "Цвет", "Красный", 2)
        );

        ProductResponse dto = mapper.toDto(entity, numFactors, boolFactors, enumFactors);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Товар");
        assertThat(dto.numericFactors()).containsExactlyElementsOf(numFactors);
        assertThat(dto.booleanFactors()).containsExactlyElementsOf(boolFactors);
        assertThat(dto.enumFactors()).containsExactlyElementsOf(enumFactors);
        // Проверяем, что базовые поля всё ещё на месте
        assertThat(dto.category().name()).isEqualTo("Кат");
        assertThat(dto.brand().name()).isEqualTo("Бренд");
        assertThat(dto.barcode()).isEqualTo("4600000000028");
    }

    @Test
    void toDto_ProductEntityWithEmptyFactorLists_ShouldMapEmptyLists() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setName("Пустой");

        List<ProductNumericFactorResponse> emptyNum = Collections.emptyList();
        List<ProductBooleanFactorResponse> emptyBool = Collections.emptyList();
        List<ProductEnumFactorResponse> emptyEnum = Collections.emptyList();

        ProductResponse dto = mapper.toDto(entity, emptyNum, emptyBool, emptyEnum);

        assertThat(dto.numericFactors()).isEmpty();
        assertThat(dto.booleanFactors()).isEmpty();
        assertThat(dto.enumFactors()).isEmpty();
    }
}
