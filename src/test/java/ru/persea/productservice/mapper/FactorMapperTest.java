package ru.persea.productservice.mapper;

import org.junit.jupiter.api.Test;
import ru.persea.productservice.dto.factor.factor.response.*;
import ru.persea.productservice.dto.factor.factorType.response.FactorTypeDto;
import ru.persea.productservice.dto.factor.unit.response.UnitDto;
import ru.persea.productservice.dto.product.category.response.CategoryDto;
import ru.persea.productservice.entity.factor.*;
import ru.persea.productservice.entity.product.CategoryEntity;

import static org.assertj.core.api.Assertions.assertThat;

class FactorMapperTest {

    private final FactorMapper mapper = new FactorMapperImpl();

    @Test
    void toDto_UnitEntity_ShouldMapCorrectly() {
        UnitEntity entity = new UnitEntity();
        entity.setId((short) 1);
        entity.setName("метр");

        UnitDto dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo((short) 1);
        assertThat(dto.name()).isEqualTo("метр");
    }

    @Test
    void toDto_FactorTypeEntity_ShouldMapCorrectly() {
        FactorTypeEntity entity = new FactorTypeEntity();
        entity.setId((short) 10);
        entity.setName("Размер");

        FactorTypeDto dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo((short) 10);
        assertThat(dto.name()).isEqualTo("Размер");
    }

    @Test
    void toDto_FactorEntity_ShouldMapCorrectly() {
        FactorTypeEntity typeEntity = new FactorTypeEntity();
        typeEntity.setId((short) 20);
        typeEntity.setName("Цвет");

        FactorEntity entity = new FactorEntity();
        entity.setId(100L);
        entity.setName("Основной цвет");
        entity.setType(typeEntity);
        entity.setDescription("описание");

        FactorDto dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(100L);
        assertThat(dto.name()).isEqualTo("Основной цвет");
        assertThat(dto.description()).isEqualTo("описание");
        assertThat(dto.type().id()).isEqualTo((short) 20);
        assertThat(dto.type().name()).isEqualTo("Цвет");
    }

    @Test
    void toDto_FactorNumericRuleEntity_ShouldMapCorrectly() {
        FactorEntity factor = new FactorEntity();
        factor.setId(1L);
        factor.setName("Вес");

        CategoryEntity category = new CategoryEntity(10L, "Категория", "cat_code");
        UnitEntity unit = new UnitEntity();
        unit.setId((short) 5);
        unit.setName("кг");

        FactorNumericRuleEntity entity = new FactorNumericRuleEntity();
        entity.setId(200L);
        entity.setFactor(factor);
        entity.setCategory(category);
        entity.setUnit(unit);
        entity.setMinValue(0.0);
        entity.setMaxValue(10.0);

        FactorNumericRuleResponse dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(200L);
        assertThat(dto.minValue()).isEqualTo(0.0);
        assertThat(dto.maxValue()).isEqualTo(10.0);
        assertThat(dto.factor().name()).isEqualTo("Вес");
        assertThat(dto.category().id()).isEqualTo(10L);
        assertThat(dto.category().name()).isEqualTo("Категория");
        assertThat(dto.unit().id()).isEqualTo((short) 5);
        assertThat(dto.unit().name()).isEqualTo("кг");
    }

    @Test
    void toDto_FactorBooleanRuleEntity_ShouldMapCorrectly() {
        FactorEntity factor = new FactorEntity();
        factor.setId(2L);
        factor.setName("Складной");

        CategoryEntity category = new CategoryEntity(20L, "Кат", "code2");

        FactorBooleanRuleEntity entity = new FactorBooleanRuleEntity();
        entity.setId(300L);
        entity.setFactor(factor);
        entity.setCategory(category);
        entity.setImpact(1);

        FactorBooleanRuleResponse dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(300L);
        assertThat(dto.impact()).isEqualTo(1);
        assertThat(dto.factor().name()).isEqualTo("Складной");
        assertThat(dto.category().id()).isEqualTo(20L);
    }

    @Test
    void toDto_FactorEnumValueEntity_ShouldMapCorrectly() {
        FactorEntity factor = new FactorEntity();
        factor.setId(3L);
        factor.setName("Цвет");

        FactorEnumValueEntity entity = new FactorEnumValueEntity();
        entity.setId(400L);
        entity.setFactor(factor);
        entity.setValue("Красный");

        FactorEnumValueResponse dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(400L);
        assertThat(dto.value()).isEqualTo("Красный");
        assertThat(dto.factor().name()).isEqualTo("Цвет");
    }

    @Test
    void toDto_FactorEnumRuleEntity_ShouldMapCorrectly() {
        CategoryEntity category = new CategoryEntity(30L, "Кат3", "c3");

        FactorEnumValueEntity enumValue = new FactorEnumValueEntity();
        enumValue.setId(500L);
        enumValue.setValue("Синий");

        FactorEnumRuleEntity entity = new FactorEnumRuleEntity();
        entity.setId(600L);
        entity.setCategory(category);
        entity.setEnumValue(enumValue);
        entity.setImpact(2);

        FactorEnumRuleResponse dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(600L);
        assertThat(dto.impact()).isEqualTo(2);
        assertThat(dto.category().id()).isEqualTo(30L);
        assertThat(dto.category().name()).isEqualTo("Кат3");
        assertThat(dto.enumValue().id()).isEqualTo(500L);
        assertThat(dto.enumValue().value()).isEqualTo("Синий");
    }
}