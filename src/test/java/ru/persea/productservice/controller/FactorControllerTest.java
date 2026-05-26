package ru.persea.productservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.persea.productservice.dto.factor.factor.request.*;
import ru.persea.productservice.dto.factor.factor.response.*;
import ru.persea.productservice.dto.factor.factorType.request.CreateFactorTypeRequest;
import ru.persea.productservice.dto.factor.factorType.request.UpdateFactorTypeRequest;
import ru.persea.productservice.dto.factor.factorType.response.FactorTypeDto;
import ru.persea.productservice.dto.factor.unit.request.CreateUnitRequest;
import ru.persea.productservice.dto.factor.unit.request.UpdateUnitRequest;
import ru.persea.productservice.dto.factor.unit.response.UnitDto;
import ru.persea.productservice.dto.product.category.response.CategoryDto;
import ru.persea.productservice.service.FactorService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FactorControllerTest {

    @Mock
    private FactorService factorService;

    @InjectMocks
    private FactorController controller;

    // ================== Unit tests ==================
    @Test
    void createUnit_shouldReturnCreated() {
        CreateUnitRequest request = new CreateUnitRequest("метр");
        UnitDto unitDto = new UnitDto((short) 1, "метр");
        when(factorService.createUnit(any(CreateUnitRequest.class))).thenReturn(unitDto);

        ResponseEntity<UnitDto> response = controller.createUnit(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(unitDto);
    }

    @Test
    void getUnits_shouldReturnOk() {
        List<UnitDto> units = List.of(new UnitDto((short) 1, "кг"));
        when(factorService.getUnits()).thenReturn(units);

        ResponseEntity<List<UnitDto>> response = controller.getUnits();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyElementsOf(units);
    }

    @Test
    void getUnit_shouldReturnOk() {
        UnitDto unit = new UnitDto((short) 2, "л");
        when(factorService.getUnit((short) 2)).thenReturn(unit);

        ResponseEntity<UnitDto> response = controller.getUnit((short) 2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(unit);
    }

    @Test
    void updateUnit_shouldReturnOk() {
        UpdateUnitRequest request = new UpdateUnitRequest("литр");
        UnitDto updated = new UnitDto((short) 2, "литр");
        when(factorService.updateUnit(eq((short) 2), any(UpdateUnitRequest.class))).thenReturn(updated);

        ResponseEntity<UnitDto> response = controller.updateUnit((short) 2, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("литр");
    }

    @Test
    void deleteUnit_shouldInvokeServiceAndReturnNoContent() {
        controller.deleteUnit((short) 1);
        verify(factorService).deleteUnit((short) 1);
    }

    // ================== FactorType tests ==================
    @Test
    void createFactorType_shouldReturnCreated() {
        CreateFactorTypeRequest request = new CreateFactorTypeRequest("Размер");
        FactorTypeDto dto = new FactorTypeDto((short) 1, "Размер");
        when(factorService.createFactorType(any())).thenReturn(dto);

        ResponseEntity<FactorTypeDto> response = controller.createFactorType(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void getFactorTypes_shouldReturnOk() {
        FactorTypeDto dto = new FactorTypeDto((short) 1, "Цвет");
        when(factorService.getFactorTypes()).thenReturn(List.of(dto));

        ResponseEntity<List<FactorTypeDto>> response = controller.getFactorTypes();

        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).name()).isEqualTo("Цвет");
    }

    @Test
    void getFactorType_shouldReturnOk() {
        FactorTypeDto dto = new FactorTypeDto((short) 3, "Материал");
        when(factorService.getFactorType((short) 3)).thenReturn(dto);

        ResponseEntity<FactorTypeDto> response = controller.getFactorType((short) 3);

        assertThat(response.getBody().name()).isEqualTo("Материал");
    }

    @Test
    void updateFactorType_shouldReturnOk() {
        UpdateFactorTypeRequest request = new UpdateFactorTypeRequest("Форма");
        FactorTypeDto updated = new FactorTypeDto((short) 3, "Форма");
        when(factorService.updateFactorType(eq((short) 3), any())).thenReturn(updated);

        ResponseEntity<FactorTypeDto> response = controller.updateFactorType((short) 3, request);

        assertThat(response.getBody().name()).isEqualTo("Форма");
    }

    @Test
    void deleteFactorType_shouldInvokeService() {
        controller.deleteFactorType((short) 2);
        verify(factorService).deleteFactorType((short) 2);
    }

    // ================== Factor tests ==================
    @Test
    void createFactor_shouldReturnCreated() {
        CreateFactorRequest request = new CreateFactorRequest("Длина", (short) 1, "описание");
        FactorTypeDto type = new FactorTypeDto((short) 1, "Размер");
        FactorDto factor = new FactorDto(10L, "Длина", type, "описание");
        when(factorService.createFactor(any())).thenReturn(factor);

        ResponseEntity<FactorDto> response = controller.createFactor(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().id()).isEqualTo(10L);
        assertThat(response.getBody().type().name()).isEqualTo("Размер");
    }

    @Test
    void getFactors_shouldReturnOk() {
        FactorDto factor = new FactorDto(1L, "Вес", null, null);
        when(factorService.getFactors()).thenReturn(List.of(factor));

        ResponseEntity<List<FactorDto>> response = controller.getFactors();

        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void getFactor_shouldReturnOk() {
        FactorDto factor = new FactorDto(5L, "Цвет", null, null);
        when(factorService.getFactor(5L)).thenReturn(factor);

        ResponseEntity<FactorDto> response = controller.getFactor(5L);

        assertThat(response.getBody().id()).isEqualTo(5L);
    }

    @Test
    void updateFactor_shouldReturnOk() {
        UpdateFactorRequest request = new UpdateFactorRequest("Обновлённый", (short) 2, "новое описание");
        FactorTypeDto type = new FactorTypeDto((short) 2, "Тип");
        FactorDto updated = new FactorDto(5L, "Обновлённый", type, "новое описание");
        when(factorService.updateFactor(eq(5L), any())).thenReturn(updated);

        ResponseEntity<FactorDto> response = controller.updateFactor(5L, request);

        assertThat(response.getBody().description()).isEqualTo("новое описание");
    }

    @Test
    void deleteFactor_shouldInvokeService() {
        controller.deleteFactor(10L);
        verify(factorService).deleteFactor(10L);
    }

    // ================== Numeric rule tests ==================
    @Test
    void createFactorNumericRule_shouldReturnCreated() {
        CreateFactorNumericRuleRequest request = new CreateFactorNumericRuleRequest(100L, (short) 1, 0.0, 10.0);
        FactorNumericRuleResponse rule = new FactorNumericRuleResponse(
                1L, null, new CategoryDto(100L, "cat", "code"), new UnitDto((short) 1, "м"), 0.0, 10.0
        );
        when(factorService.createFactorNumericRule(eq(5L), any())).thenReturn(rule);

        ResponseEntity<FactorNumericRuleResponse> response = controller.createFactorNumericRule(5L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().category().id()).isEqualTo(100L);
    }

    @Test
    void getFactorNumericRule_shouldReturnOk() {
        FactorNumericRuleResponse rule = new FactorNumericRuleResponse(2L, null, null, null, 1.0, 5.0);
        when(factorService.getFactorNumericRule(2L)).thenReturn(rule);

        ResponseEntity<FactorNumericRuleResponse> response = controller.getFactorNumericRule(2L);

        assertThat(response.getBody().minValue()).isEqualTo(1.0);
    }

    @Test
    void updateFactorNumericRule_shouldReturnOk() {
        UpdateFactorNumericRuleRequest request = new UpdateFactorNumericRuleRequest(200L, (short) 2, 5.0, 10.0);
        FactorNumericRuleResponse updated = new FactorNumericRuleResponse(
                2L, null, new CategoryDto(200L, "cat2", "c2"), new UnitDto((short) 2, "кг"), 5.0, 10.0
        );
        when(factorService.updateFactorNumericRule(eq(2L), any())).thenReturn(updated);

        ResponseEntity<FactorNumericRuleResponse> response = controller.updateFactorNumericRule(2L, request);

        assertThat(response.getBody().unit().name()).isEqualTo("кг");
    }

    @Test
    void deleteFactorNumericRule_shouldInvokeService() {
        controller.deleteFactorNumericRule(2L);
        verify(factorService).deleteFactorNumericRule(2L);
    }

    // ================== Boolean rule tests ==================
    @Test
    void createFactorBooleanRule_shouldReturnCreated() {
        CreateFactorBooleanRuleRequest request = new CreateFactorBooleanRuleRequest(300L, 1);
        FactorBooleanRuleResponse rule = new FactorBooleanRuleResponse(1L, null, new CategoryDto(300L, "cat3", "c3"), 1);
        when(factorService.createFactorBooleanRule(eq(5L), any())).thenReturn(rule);

        ResponseEntity<FactorBooleanRuleResponse> response = controller.createFactorBooleanRule(5L, request);

        assertThat(response.getBody().impact()).isEqualTo(1);
    }

    @Test
    void getFactorBooleanRule_shouldReturnOk() {
        FactorBooleanRuleResponse rule = new FactorBooleanRuleResponse(3L, null, null, 0);
        when(factorService.getFactorBooleanRule(3L)).thenReturn(rule);

        ResponseEntity<FactorBooleanRuleResponse> response = controller.getFactorBooleanRule(3L);

        assertThat(response.getBody().id()).isEqualTo(3L);
    }

    @Test
    void updateFactorBooleanRule_shouldReturnOk() {
        UpdateFactorBooleanRuleRequest request = new UpdateFactorBooleanRuleRequest(400L, 1);
        FactorBooleanRuleResponse updated = new FactorBooleanRuleResponse(3L, null, new CategoryDto(400L, "cat4", "c4"), 1);
        when(factorService.updateFactorBooleanRule(eq(3L), any())).thenReturn(updated);

        ResponseEntity<FactorBooleanRuleResponse> response = controller.updateFactorBooleanRule(3L, request);

        assertThat(response.getBody().category().id()).isEqualTo(400L);
    }

    @Test
    void deleteFactorBooleanRule_shouldInvokeService() {
        controller.deleteFactorBooleanRule(3L);
        verify(factorService).deleteFactorBooleanRule(3L);
    }

    // ================== Enum value tests ==================
    @Test
    void createFactorEnumValue_shouldReturnCreated() {
        CreateFactorEnumValueRequest request = new CreateFactorEnumValueRequest("Красный");
        FactorEnumValueResponse value = new FactorEnumValueResponse(1L, null, "Красный");
        when(factorService.createFactorEnumValue(eq(5L), any())).thenReturn(value);

        ResponseEntity<FactorEnumValueResponse> response = controller.createFactorEnumValue(5L, request);

        assertThat(response.getBody().value()).isEqualTo("Красный");
    }

    @Test
    void getFactorEnumValue_shouldReturnOk() {
        FactorEnumValueResponse value = new FactorEnumValueResponse(2L, null, "Синий");
        when(factorService.getFactorEnumValue(2L)).thenReturn(value);

        ResponseEntity<FactorEnumValueResponse> response = controller.getFactorEnumValue(2L);

        assertThat(response.getBody().value()).isEqualTo("Синий");
    }

    @Test
    void updateFactorEnumValue_shouldReturnOk() {
        UpdateFactorEnumValueRequest request = new UpdateFactorEnumValueRequest("Зелёный");
        FactorEnumValueResponse updated = new FactorEnumValueResponse(2L, null, "Зелёный");
        when(factorService.updateFactorEnumValue(eq(2L), any())).thenReturn(updated);

        ResponseEntity<FactorEnumValueResponse> response = controller.updateFactorEnumValue(2L, request);

        assertThat(response.getBody().value()).isEqualTo("Зелёный");
    }

    @Test
    void deleteFactorEnumValue_shouldInvokeService() {
        controller.deleteFactorEnumValue(2L);
        verify(factorService).deleteFactorEnumValue(2L);
    }

    // ================== Enum rule tests ==================
    @Test
    void createFactorEnumRule_shouldReturnCreated() {
        CreateFactorEnumRuleRequest request = new CreateFactorEnumRuleRequest(500L, 2);
        FactorEnumRuleResponse rule = new FactorEnumRuleResponse(1L, new CategoryDto(500L, "cat5", "c5"), null, 2);
        when(factorService.createFactorEnumRule(eq(10L), any())).thenReturn(rule);

        ResponseEntity<FactorEnumRuleResponse> response = controller.createFactorEnumRule(10L, request);

        assertThat(response.getBody().impact()).isEqualTo(2);
    }

    @Test
    void getFactorEnumRule_shouldReturnOk() {
        FactorEnumRuleResponse rule = new FactorEnumRuleResponse(3L, null, null, 1);
        when(factorService.getFactorEnumRule(3L)).thenReturn(rule);

        ResponseEntity<FactorEnumRuleResponse> response = controller.getFactorEnumRule(3L);

        assertThat(response.getBody().id()).isEqualTo(3L);
    }

    @Test
    void updateFactorEnumRule_shouldReturnOk() {
        UpdateFactorEnumRuleRequest request = new UpdateFactorEnumRuleRequest(600L, 1L, 3);
        FactorEnumRuleResponse updated = new FactorEnumRuleResponse(3L, new CategoryDto(600L, "cat6", "c6"), null, 3);
        when(factorService.updateFactorEnumRule(eq(3L), any())).thenReturn(updated);

        ResponseEntity<FactorEnumRuleResponse> response = controller.updateFactorEnumRule(3L, request);

        assertThat(response.getBody().category().id()).isEqualTo(600L);
    }

    @Test
    void deleteFactorEnumRule_shouldInvokeService() {
        controller.deleteFactorEnumRule(3L);
        verify(factorService).deleteFactorEnumRule(3L);
    }
}