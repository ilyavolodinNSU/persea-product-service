package ru.persea.productservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.persea.productservice.dto.factor.factor.request.*;
import ru.persea.productservice.dto.factor.factor.response.*;
import ru.persea.productservice.dto.factor.factorType.request.CreateFactorTypeRequest;
import ru.persea.productservice.dto.factor.factorType.request.UpdateFactorTypeRequest;
import ru.persea.productservice.dto.factor.factorType.response.FactorTypeDto;
import ru.persea.productservice.dto.factor.unit.request.CreateUnitRequest;
import ru.persea.productservice.dto.factor.unit.request.UpdateUnitRequest;
import ru.persea.productservice.dto.factor.unit.response.UnitDto;
import ru.persea.productservice.entity.factor.*;
import ru.persea.productservice.entity.product.CategoryEntity;
import ru.persea.productservice.mapper.FactorMapper;
import ru.persea.productservice.repository.factor.*;
import ru.persea.productservice.repository.product.CategoryRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FactorServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private FactorTypeRepository factorTypeRepository;

    @Mock
    private FactorRepository factorRepository;

    @Mock
    private FactorNumericRuleRepository factorNumericRuleRepository;

    @Mock
    private FactorBooleanRuleRepository factorBooleanRuleRepository;

    @Mock
    private FactorEnumValueRepository factorEnumValueRepository;

    @Mock
    private FactorEnumRuleRepository factorEnumRuleRepository;

    @Mock
    private FactorMapper factorMapper;

    @InjectMocks
    private FactorServiceImpl factorService;

    // ========== Unit tests ==========
    @Test
    void createUnit_shouldSaveAndReturnDto() {
        CreateUnitRequest request = new CreateUnitRequest("метр");
        UnitEntity unitEntity = new UnitEntity();
        unitEntity.setName("метр");
        UnitEntity savedEntity = new UnitEntity();
        savedEntity.setId((short) 1);
        savedEntity.setName("метр");
        UnitDto unitDto = new UnitDto((short) 1, "метр");

        when(unitRepository.save(any(UnitEntity.class))).thenReturn(savedEntity);
        when(factorMapper.toDto(savedEntity)).thenReturn(unitDto);

        UnitDto result = factorService.createUnit(request);

        assertThat(result).isEqualTo(unitDto);
        verify(unitRepository).save(any(UnitEntity.class));
    }

    @Test
    void getUnits_shouldReturnAllDtos() {
        UnitEntity entity = new UnitEntity();
        entity.setId((short) 1);
        entity.setName("кг");
        UnitDto dto = new UnitDto((short) 1, "кг");

        when(unitRepository.findAll()).thenReturn(List.of(entity));
        when(factorMapper.toDto(entity)).thenReturn(dto);

        List<UnitDto> result = factorService.getUnits();

        assertThat(result).containsExactly(dto);
    }

    @Test
    void getUnit_existingId_shouldReturnDto() {
        UnitEntity entity = new UnitEntity();
        entity.setId((short) 2);
        entity.setName("л");
        UnitDto dto = new UnitDto((short) 2, "л");

        when(unitRepository.findById((short) 2)).thenReturn(Optional.of(entity));
        when(factorMapper.toDto(entity)).thenReturn(dto);

        UnitDto result = factorService.getUnit((short) 2);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getUnit_nonExistingId_shouldThrowEntityNotFound() {
        when(unitRepository.findById((short) 999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> factorService.getUnit((short) 999))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Единица измерения не найдена: 999");
    }

    @Test
    void updateUnit_shouldUpdateAndReturnDto() {
        UnitEntity existing = new UnitEntity();
        existing.setId((short) 2);
        existing.setName("л");

        UpdateUnitRequest request = new UpdateUnitRequest("литр");
        UnitEntity savedEntity = new UnitEntity();
        savedEntity.setId((short) 2);
        savedEntity.setName("литр");
        UnitDto dto = new UnitDto((short) 2, "литр");

        when(unitRepository.findById((short) 2)).thenReturn(Optional.of(existing));
        when(unitRepository.save(existing)).thenReturn(savedEntity);
        when(factorMapper.toDto(savedEntity)).thenReturn(dto);

        UnitDto result = factorService.updateUnit((short) 2, request);

        assertThat(result).isEqualTo(dto);
        assertThat(existing.getName()).isEqualTo("литр");
    }

    @Test
    void deleteUnit_shouldCallDeleteById() {
        factorService.deleteUnit((short) 1);
        verify(unitRepository).deleteById((short) 1);
    }

    // ========== FactorType tests ==========
    @Test
    void createFactorType_shouldSaveAndReturnDto() {
        CreateFactorTypeRequest request = new CreateFactorTypeRequest("Размер");
        FactorTypeEntity savedEntity = new FactorTypeEntity();
        savedEntity.setId((short) 1);
        savedEntity.setName("Размер");
        FactorTypeDto dto = new FactorTypeDto((short) 1, "Размер");

        when(factorTypeRepository.save(any(FactorTypeEntity.class))).thenReturn(savedEntity);
        when(factorMapper.toDto(savedEntity)).thenReturn(dto);

        FactorTypeDto result = factorService.createFactorType(request);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getFactorTypes_shouldReturnList() {
        FactorTypeEntity entity = new FactorTypeEntity();
        entity.setId((short) 1);
        entity.setName("Цвет");
        FactorTypeDto dto = new FactorTypeDto((short) 1, "Цвет");

        when(factorTypeRepository.findAll()).thenReturn(List.of(entity));
        when(factorMapper.toDto(entity)).thenReturn(dto);

        List<FactorTypeDto> result = factorService.getFactorTypes();

        assertThat(result).containsExactly(dto);
    }

    @Test
    void getFactorType_nonExisting_shouldThrow() {
        when(factorTypeRepository.findById((short) 5)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> factorService.getFactorType((short) 5))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Тип фактора не найден: 5");
    }

    @Test
    void updateFactorType_shouldUpdateAndReturnDto() {
        FactorTypeEntity existing = new FactorTypeEntity();
        existing.setId((short) 3);
        existing.setName("Материал");

        UpdateFactorTypeRequest request = new UpdateFactorTypeRequest("Форма");
        FactorTypeEntity saved = new FactorTypeEntity();
        saved.setId((short) 3);
        saved.setName("Форма");
        FactorTypeDto dto = new FactorTypeDto((short) 3, "Форма");

        when(factorTypeRepository.findById((short) 3)).thenReturn(Optional.of(existing));
        when(factorTypeRepository.save(existing)).thenReturn(saved);
        when(factorMapper.toDto(saved)).thenReturn(dto);

        FactorTypeDto result = factorService.updateFactorType((short) 3, request);

        assertThat(result).isEqualTo(dto);
        assertThat(existing.getName()).isEqualTo("Форма");
    }

    @Test
    void deleteFactorType_shouldDeleteById() {
        factorService.deleteFactorType((short) 1);
        verify(factorTypeRepository).deleteById((short) 1);
    }

    // ========== Factor tests ==========
    @Test
    void createFactor_shouldSaveAndReturnDto() {
        CreateFactorRequest request = new CreateFactorRequest("Длина", (short) 1, "описание");
        FactorTypeEntity typeEntity = new FactorTypeEntity();
        typeEntity.setId((short) 1);
        typeEntity.setName("Размер");

        FactorEntity savedEntity = new FactorEntity();
        savedEntity.setId(10L);
        savedEntity.setName("Длина");
        savedEntity.setType(typeEntity);
        savedEntity.setDescription("описание");

        FactorDto dto = new FactorDto(10L, "Длина",
                new FactorTypeDto((short) 1, "Размер"), "описание");

        when(factorTypeRepository.findById((short) 1)).thenReturn(Optional.of(typeEntity));
        when(factorRepository.save(any(FactorEntity.class))).thenReturn(savedEntity);
        when(factorMapper.toDto(savedEntity)).thenReturn(dto);

        FactorDto result = factorService.createFactor(request);

        assertThat(result).isEqualTo(dto);
        verify(factorTypeRepository).findById((short) 1);
    }

    @Test
    void getFactors_shouldReturnList() {
        FactorEntity entity = new FactorEntity();
        entity.setId(1L);
        entity.setName("Вес");
        FactorDto dto = new FactorDto(1L, "Вес", null, null);

        when(factorRepository.findAll()).thenReturn(List.of(entity));
        when(factorMapper.toDto(entity)).thenReturn(dto);

        List<FactorDto> result = factorService.getFactors();

        assertThat(result).containsExactly(dto);
    }

    @Test
    void getFactor_nonExisting_shouldThrow() {
        when(factorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> factorService.getFactor(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Фактор не найден: 999");
    }

    @Test
    void updateFactor_shouldUpdateAndReturnDto() {
        FactorEntity existing = new FactorEntity();
        existing.setId(5L);
        existing.setName("Цвет");

        FactorTypeEntity typeEntity = new FactorTypeEntity();
        typeEntity.setId((short) 2);
        typeEntity.setName("Тип");

        UpdateFactorRequest request = new UpdateFactorRequest("Обновлённый", (short) 2, "новое описание");

        FactorEntity saved = new FactorEntity();
        saved.setId(5L);
        saved.setName("Обновлённый");
        saved.setType(typeEntity);
        saved.setDescription("новое описание");

        FactorDto dto = new FactorDto(5L, "Обновлённый",
                new FactorTypeDto((short) 2, "Тип"), "новое описание");

        when(factorRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(factorTypeRepository.findById((short) 2)).thenReturn(Optional.of(typeEntity));
        when(factorRepository.save(existing)).thenReturn(saved);
        when(factorMapper.toDto(saved)).thenReturn(dto);

        FactorDto result = factorService.updateFactor(5L, request);

        assertThat(result).isEqualTo(dto);
        assertThat(existing.getName()).isEqualTo("Обновлённый");
        assertThat(existing.getType()).isEqualTo(typeEntity);
    }

    @Test
    void deleteFactor_shouldDeleteById() {
        factorService.deleteFactor(10L);
        verify(factorRepository).deleteById(10L);
    }

    // ========== Numeric rule tests ==========
    @Test
    void createFactorNumericRule_shouldSaveAndReturnDto() {
        CreateFactorNumericRuleRequest request = new CreateFactorNumericRuleRequest(100L, (short) 1, 0.0, 10.0);
        CategoryEntity categoryProxy = new CategoryEntity();
        categoryProxy.setId(100L);
        FactorEntity factorProxy = new FactorEntity();
        factorProxy.setId(5L);
        UnitEntity unitProxy = new UnitEntity();
        unitProxy.setId((short) 1);

        FactorNumericRuleEntity savedEntity = new FactorNumericRuleEntity();
        savedEntity.setId(1L);
        FactorNumericRuleResponse responseDto = new FactorNumericRuleResponse(1L, null, null, null, 0.0, 10.0);

        when(categoryRepository.getReferenceById(100L)).thenReturn(categoryProxy);
        when(factorRepository.getReferenceById(5L)).thenReturn(factorProxy);
        when(unitRepository.getReferenceById((short) 1)).thenReturn(unitProxy);
        when(factorNumericRuleRepository.save(any(FactorNumericRuleEntity.class))).thenReturn(savedEntity);
        when(factorMapper.toDto(savedEntity)).thenReturn(responseDto);

        FactorNumericRuleResponse result = factorService.createFactorNumericRule(5L, request);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void getFactorNumericRule_existing_shouldReturnDto() {
        FactorNumericRuleEntity entity = new FactorNumericRuleEntity();
        entity.setId(2L);
        FactorNumericRuleResponse dto = new FactorNumericRuleResponse(2L, null, null, null, 1.0, 5.0);

        when(factorNumericRuleRepository.findById(2L)).thenReturn(Optional.of(entity));
        when(factorMapper.toDto(entity)).thenReturn(dto);

        FactorNumericRuleResponse result = factorService.getFactorNumericRule(2L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getFactorNumericRule_nonExisting_shouldThrow() {
        when(factorNumericRuleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> factorService.getFactorNumericRule(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Numeric правило не найдено: 999");
    }

    @Test
    void updateFactorNumericRule_shouldUpdateAndReturnDto() {
        UpdateFactorNumericRuleRequest request = new UpdateFactorNumericRuleRequest(200L, (short) 2, 5.0, 10.0);
        FactorNumericRuleEntity existing = new FactorNumericRuleEntity();
        existing.setId(2L);

        CategoryEntity categoryProxy = new CategoryEntity();
        categoryProxy.setId(200L);
        UnitEntity unitProxy = new UnitEntity();
        unitProxy.setId((short) 2);

        FactorNumericRuleEntity savedEntity = new FactorNumericRuleEntity();
        savedEntity.setId(2L);
        FactorNumericRuleResponse responseDto = new FactorNumericRuleResponse(2L, null, null, null, 5.0, 10.0);

        when(factorNumericRuleRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(categoryRepository.getReferenceById(200L)).thenReturn(categoryProxy);
        when(unitRepository.getReferenceById((short) 2)).thenReturn(unitProxy);
        when(factorNumericRuleRepository.save(existing)).thenReturn(savedEntity);
        when(factorMapper.toDto(savedEntity)).thenReturn(responseDto);

        FactorNumericRuleResponse result = factorService.updateFactorNumericRule(2L, request);

        assertThat(result).isEqualTo(responseDto);
        assertThat(existing.getCategory()).isEqualTo(categoryProxy);
        assertThat(existing.getUnit()).isEqualTo(unitProxy);
    }

    @Test
    void deleteFactorNumericRule_shouldDeleteById() {
        factorService.deleteFactorNumericRule(2L);
        verify(factorNumericRuleRepository).deleteById(2L);
    }

    // ========== Boolean rule tests ==========
    @Test
    void createFactorBooleanRule_shouldSaveAndReturnDto() {
        CreateFactorBooleanRuleRequest request = new CreateFactorBooleanRuleRequest(300L, 1);
        CategoryEntity categoryProxy = new CategoryEntity();
        categoryProxy.setId(300L);
        FactorEntity factorProxy = new FactorEntity();
        factorProxy.setId(5L);

        FactorBooleanRuleEntity savedEntity = new FactorBooleanRuleEntity();
        savedEntity.setId(1L);
        FactorBooleanRuleResponse responseDto = new FactorBooleanRuleResponse(1L, null, null, 1);

        when(categoryRepository.getReferenceById(300L)).thenReturn(categoryProxy);
        when(factorRepository.getReferenceById(5L)).thenReturn(factorProxy);
        when(factorBooleanRuleRepository.save(any(FactorBooleanRuleEntity.class))).thenReturn(savedEntity);
        when(factorMapper.toDto(savedEntity)).thenReturn(responseDto);

        FactorBooleanRuleResponse result = factorService.createFactorBooleanRule(5L, request);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void getFactorBooleanRule_existing_shouldReturnDto() {
        FactorBooleanRuleEntity entity = new FactorBooleanRuleEntity();
        entity.setId(3L);
        FactorBooleanRuleResponse dto = new FactorBooleanRuleResponse(3L, null, null, 0);

        when(factorBooleanRuleRepository.findById(3L)).thenReturn(Optional.of(entity));
        when(factorMapper.toDto(entity)).thenReturn(dto);

        FactorBooleanRuleResponse result = factorService.getFactorBooleanRule(3L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getFactorBooleanRule_nonExisting_shouldThrow() {
        when(factorBooleanRuleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> factorService.getFactorBooleanRule(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Boolean правило не найдено: 999");
    }

    @Test
    void updateFactorBooleanRule_shouldUpdateAndReturnDto() {
        UpdateFactorBooleanRuleRequest request = new UpdateFactorBooleanRuleRequest(400L, 1);
        FactorBooleanRuleEntity existing = new FactorBooleanRuleEntity();
        existing.setId(3L);
        CategoryEntity categoryProxy = new CategoryEntity();
        categoryProxy.setId(400L);

        FactorBooleanRuleEntity saved = new FactorBooleanRuleEntity();
        saved.setId(3L);
        FactorBooleanRuleResponse dto = new FactorBooleanRuleResponse(3L, null, null, 1);

        when(factorBooleanRuleRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(categoryRepository.getReferenceById(400L)).thenReturn(categoryProxy);
        when(factorBooleanRuleRepository.save(existing)).thenReturn(saved);
        when(factorMapper.toDto(saved)).thenReturn(dto);

        FactorBooleanRuleResponse result = factorService.updateFactorBooleanRule(3L, request);

        assertThat(result).isEqualTo(dto);
        assertThat(existing.getCategory()).isEqualTo(categoryProxy);
    }

    @Test
    void deleteFactorBooleanRule_shouldDeleteById() {
        factorService.deleteFactorBooleanRule(3L);
        verify(factorBooleanRuleRepository).deleteById(3L);
    }

    // ========== Enum value tests ==========
    @Test
    void createFactorEnumValue_shouldSaveAndReturnDto() {
        CreateFactorEnumValueRequest request = new CreateFactorEnumValueRequest("Красный");
        FactorEntity factorProxy = new FactorEntity();
        factorProxy.setId(5L);

        FactorEnumValueEntity savedEntity = new FactorEnumValueEntity();
        savedEntity.setId(1L);
        FactorEnumValueResponse dto = new FactorEnumValueResponse(1L, null, "Красный");

        when(factorRepository.getReferenceById(5L)).thenReturn(factorProxy);
        when(factorEnumValueRepository.save(any(FactorEnumValueEntity.class))).thenReturn(savedEntity);
        when(factorMapper.toDto(savedEntity)).thenReturn(dto);

        FactorEnumValueResponse result = factorService.createFactorEnumValue(5L, request);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getFactorEnumValue_existing_shouldReturnDto() {
        FactorEnumValueEntity entity = new FactorEnumValueEntity();
        entity.setId(2L);
        entity.setValue("Синий");
        FactorEnumValueResponse dto = new FactorEnumValueResponse(2L, null, "Синий");

        when(factorEnumValueRepository.findById(2L)).thenReturn(Optional.of(entity));
        when(factorMapper.toDto(entity)).thenReturn(dto);

        FactorEnumValueResponse result = factorService.getFactorEnumValue(2L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getFactorEnumValue_nonExisting_shouldThrow() {
        when(factorEnumValueRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> factorService.getFactorEnumValue(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Enum значение не найдено: 999");
    }

    @Test
    void updateFactorEnumValue_shouldUpdateAndReturnDto() {
        UpdateFactorEnumValueRequest request = new UpdateFactorEnumValueRequest("Зелёный");
        FactorEnumValueEntity existing = new FactorEnumValueEntity();
        existing.setId(2L);
        existing.setValue("Синий");

        FactorEnumValueEntity saved = new FactorEnumValueEntity();
        saved.setId(2L);
        saved.setValue("Зелёный");
        FactorEnumValueResponse dto = new FactorEnumValueResponse(2L, null, "Зелёный");

        when(factorEnumValueRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(factorEnumValueRepository.save(existing)).thenReturn(saved);
        when(factorMapper.toDto(saved)).thenReturn(dto);

        FactorEnumValueResponse result = factorService.updateFactorEnumValue(2L, request);

        assertThat(result).isEqualTo(dto);
        assertThat(existing.getValue()).isEqualTo("Зелёный");
    }

    @Test
    void deleteFactorEnumValue_shouldDeleteById() {
        factorService.deleteFactorEnumValue(2L);
        verify(factorEnumValueRepository).deleteById(2L);
    }

    // ========== Enum rule tests ==========
    @Test
    void createFactorEnumRule_shouldSaveAndReturnDto() {
        CreateFactorEnumRuleRequest request = new CreateFactorEnumRuleRequest(500L, 2);
        CategoryEntity categoryProxy = new CategoryEntity();
        categoryProxy.setId(500L);
        FactorEnumValueEntity enumValueProxy = new FactorEnumValueEntity();
        enumValueProxy.setId(10L);

        FactorEnumRuleEntity savedEntity = new FactorEnumRuleEntity();
        savedEntity.setId(1L);
        FactorEnumRuleResponse dto = new FactorEnumRuleResponse(1L, null, null, 2);

        when(categoryRepository.getReferenceById(500L)).thenReturn(categoryProxy);
        when(factorEnumValueRepository.getReferenceById(10L)).thenReturn(enumValueProxy);
        when(factorEnumRuleRepository.save(any(FactorEnumRuleEntity.class))).thenReturn(savedEntity);
        when(factorMapper.toDto(savedEntity)).thenReturn(dto);

        FactorEnumRuleResponse result = factorService.createFactorEnumRule(10L, request);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getFactorEnumRule_existing_shouldReturnDto() {
        FactorEnumRuleEntity entity = new FactorEnumRuleEntity();
        entity.setId(3L);
        FactorEnumRuleResponse dto = new FactorEnumRuleResponse(3L, null, null, 1);

        when(factorEnumRuleRepository.findById(3L)).thenReturn(Optional.of(entity));
        when(factorMapper.toDto(entity)).thenReturn(dto);

        FactorEnumRuleResponse result = factorService.getFactorEnumRule(3L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getFactorEnumRule_nonExisting_shouldThrow() {
        when(factorEnumRuleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> factorService.getFactorEnumRule(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Enum правило не найдено: 999");
    }

    @Test
    void updateFactorEnumRule_shouldUpdateAndReturnDto() {
        UpdateFactorEnumRuleRequest request = new UpdateFactorEnumRuleRequest(600L, 1L, 3);
        FactorEnumRuleEntity existing = new FactorEnumRuleEntity();
        existing.setId(3L);

        CategoryEntity categoryProxy = new CategoryEntity();
        categoryProxy.setId(600L);
        FactorEnumValueEntity enumValueProxy = new FactorEnumValueEntity();
        enumValueProxy.setId(1L);

        FactorEnumRuleEntity saved = new FactorEnumRuleEntity();
        saved.setId(3L);
        FactorEnumRuleResponse dto = new FactorEnumRuleResponse(3L, null, null, 3);

        when(factorEnumRuleRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(categoryRepository.getReferenceById(600L)).thenReturn(categoryProxy);
        when(factorEnumValueRepository.getReferenceById(1L)).thenReturn(enumValueProxy);
        when(factorEnumRuleRepository.save(existing)).thenReturn(saved);
        when(factorMapper.toDto(saved)).thenReturn(dto);

        FactorEnumRuleResponse result = factorService.updateFactorEnumRule(3L, request);

        assertThat(result).isEqualTo(dto);
        assertThat(existing.getCategory()).isEqualTo(categoryProxy);
        assertThat(existing.getEnumValue()).isEqualTo(enumValueProxy);
    }

    @Test
    void deleteFactorEnumRule_shouldDeleteById() {
        factorService.deleteFactorEnumRule(3L);
        verify(factorEnumRuleRepository).deleteById(3L);
    }
}