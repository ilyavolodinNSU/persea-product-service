package ru.persea.productservice.mapper;

import org.junit.jupiter.api.Test;
import ru.persea.productservice.dto.product.product.ProductSearchDto;
import ru.persea.productservice.entity.product.ProductDocument;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ProductSearchMapperTest {

    private final ProductSearchMapper mapper = new ProductSearchMapperImpl();

    @Test
    void toDto_ShouldMapCorrectly() {
        Instant now = Instant.now();
        ProductDocument document = new ProductDocument(
                1L,
                "Тестовый товар",
                10,     // categoryId
                20,     // brandId
                5,      // rating
                "http://image.url",
                now
        );

        ProductSearchDto dto = mapper.toDto(document);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Тестовый товар");
        assertThat(dto.rating()).isEqualTo(5);
        assertThat(dto.imageURI()).isEqualTo("http://image.url");
    }
}