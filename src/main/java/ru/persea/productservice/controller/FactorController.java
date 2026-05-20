package ru.persea.productservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.factor.factor.request.*;
import ru.persea.productservice.dto.factor.factor.response.FactorDto;
import ru.persea.productservice.dto.factor.factorType.request.CreateFactorTypeRequest;
import ru.persea.productservice.dto.factor.factorType.request.UpdateFactorTypeRequest;
import ru.persea.productservice.dto.factor.factorType.response.FactorTypeDto;
import ru.persea.productservice.dto.factor.unit.request.CreateUnitRequest;
import ru.persea.productservice.dto.factor.unit.request.UpdateUnitRequest;
import ru.persea.productservice.dto.factor.unit.response.UnitDto;
import ru.persea.productservice.dto.factor.factor.response.FactorBooleanRuleResponse;
import ru.persea.productservice.dto.factor.factor.response.FactorEnumRuleResponse;
import ru.persea.productservice.dto.factor.factor.response.FactorEnumValueResponse;
import ru.persea.productservice.dto.factor.factor.response.FactorNumericRuleResponse;
import ru.persea.productservice.service.FactorService;
import java.util.List;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/factors")
@RequiredArgsConstructor
public class FactorController {
    private final FactorService factorService;

    // units

    @PostMapping("/units")
    public ResponseEntity<UnitDto> createUnit(@Valid @RequestBody CreateUnitRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(factorService.createUnit(request));
    }

    @GetMapping("/units")
    public ResponseEntity<List<UnitDto>> getUnits() {
        return ResponseEntity.ok(factorService.getUnits());
    }

    @GetMapping("/units/{id}")
    public ResponseEntity<UnitDto> getUnit(@PathVariable Short id) {
        return ResponseEntity.ok(factorService.getUnit(id));
    }

    @PutMapping("/units/{id}")
    public ResponseEntity<UnitDto> updateUnit(
            @PathVariable Short id,
            @Valid @RequestBody UpdateUnitRequest request
    ) {
        return ResponseEntity.ok(factorService.updateUnit(id, request));
    }

    @DeleteMapping("/units/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUnit(@PathVariable Short id) {
        factorService.deleteUnit(id);
    }

    // factor types

    @PostMapping("/types")
    public ResponseEntity<FactorTypeDto> createFactorType(
            @Valid @RequestBody CreateFactorTypeRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(factorService.createFactorType(request));
    }

    @GetMapping("/types")
    public ResponseEntity<List<FactorTypeDto>> getFactorTypes() {
        return ResponseEntity.ok(factorService.getFactorTypes());
    }

    @GetMapping("/types/{id}")
    public ResponseEntity<FactorTypeDto> getFactorType(@PathVariable Short id) {
        return ResponseEntity.ok(factorService.getFactorType(id));
    }

    @PutMapping("/types/{id}")
    public ResponseEntity<FactorTypeDto> updateFactorType(
            @PathVariable Short id,
            @Valid @RequestBody UpdateFactorTypeRequest request
    ) {
        return ResponseEntity.ok(factorService.updateFactorType(id, request));
    }

    @DeleteMapping("/types/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFactorType(@PathVariable Short id) {
        factorService.deleteFactorType(id);
    }

    // factors

    @PostMapping
    public ResponseEntity<FactorDto> createFactor(
            @Valid @RequestBody CreateFactorRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(factorService.createFactor(request));
    }

    @GetMapping
    public ResponseEntity<List<FactorDto>> getFactors() {
        return ResponseEntity.ok(factorService.getFactors());
    }

    @GetMapping("/{factorId}")
    public ResponseEntity<FactorDto> getFactor(@PathVariable Long factorId) {
        return ResponseEntity.ok(factorService.getFactor(factorId));
    }

    @PutMapping("/{factorId}")
    public ResponseEntity<FactorDto> updateFactor(
            @PathVariable Long factorId,
            @Valid @RequestBody UpdateFactorRequest request
    ) {
        return ResponseEntity.ok(factorService.updateFactor(factorId, request));
    }

    @DeleteMapping("/{factorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFactor(@PathVariable Long factorId) {
        factorService.deleteFactor(factorId);
    }

    // numeric rules

    @PostMapping("/{factorId}/numeric-rules")
    public ResponseEntity<FactorNumericRuleResponse> createFactorNumericRule(
            @PathVariable Long factorId,
            @Valid @RequestBody CreateFactorNumericRuleRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(factorService.createFactorNumericRule(factorId, request));
    }

    @GetMapping("/numeric-rules/{ruleId}")
    public ResponseEntity<FactorNumericRuleResponse> getFactorNumericRule(@PathVariable Long ruleId) {
        return ResponseEntity.ok(factorService.getFactorNumericRule(ruleId));
    }

    @PutMapping("/numeric-rules/{ruleId}")
    public ResponseEntity<FactorNumericRuleResponse> updateFactorNumericRule(
            @PathVariable Long ruleId,
            @Valid @RequestBody UpdateFactorNumericRuleRequest request
    ) {
        return ResponseEntity.ok(factorService.updateFactorNumericRule(ruleId, request));
    }

    @DeleteMapping("/numeric-rules/{ruleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFactorNumericRule(@PathVariable Long ruleId) {
        factorService.deleteFactorNumericRule(ruleId);
    }

    // boolean rules

    @PostMapping("/{factorId}/boolean-rules")
    public ResponseEntity<FactorBooleanRuleResponse> createFactorBooleanRule(
            @PathVariable Long factorId,
            @Valid @RequestBody CreateFactorBooleanRuleRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(factorService.createFactorBooleanRule(factorId, request));
    }

    @GetMapping("/boolean-rules/{ruleId}")
    public ResponseEntity<FactorBooleanRuleResponse> getFactorBooleanRule(@PathVariable Long ruleId) {
        return ResponseEntity.ok(factorService.getFactorBooleanRule(ruleId));
    }

    @PutMapping("/boolean-rules/{ruleId}")
    public ResponseEntity<FactorBooleanRuleResponse> updateFactorBooleanRule(
            @PathVariable Long ruleId,
            @Valid @RequestBody UpdateFactorBooleanRuleRequest request
    ) {
        return ResponseEntity.ok(factorService.updateFactorBooleanRule(ruleId, request));
    }

    @DeleteMapping("/boolean-rules/{ruleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFactorBooleanRule(@PathVariable Long ruleId) {
        factorService.deleteFactorBooleanRule(ruleId);
    }

    // enum values

    @PostMapping("/{factorId}/enum-values")
    public ResponseEntity<FactorEnumValueResponse> createFactorEnumValue(
            @PathVariable Long factorId,
            @Valid @RequestBody CreateFactorEnumValueRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(factorService.createFactorEnumValue(factorId, request));
    }

    @GetMapping("/enum-values/{valueId}")
    public ResponseEntity<FactorEnumValueResponse> getFactorEnumValue(@PathVariable Long valueId) {
        return ResponseEntity.ok(factorService.getFactorEnumValue(valueId));
    }

    @PutMapping("/enum-values/{valueId}")
    public ResponseEntity<FactorEnumValueResponse> updateFactorEnumValue(
            @PathVariable Long valueId,
            @Valid @RequestBody UpdateFactorEnumValueRequest request
    ) {
        return ResponseEntity.ok(factorService.updateFactorEnumValue(valueId, request));
    }

    @DeleteMapping("/enum-values/{valueId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFactorEnumValue(@PathVariable Long valueId) {
        factorService.deleteFactorEnumValue(valueId);
    }

    // enum rules

    @PostMapping("/enum-values/{valueId}/enum-rules")
    public ResponseEntity<FactorEnumRuleResponse> createFactorEnumRule(
        @PathVariable Long valueId,
        @Valid @RequestBody CreateFactorEnumRuleRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(factorService.createFactorEnumRule(valueId, request));
    }

    @GetMapping("/enum-rules/{ruleId}")
    public ResponseEntity<FactorEnumRuleResponse> getFactorEnumRule(@PathVariable Long ruleId) {
        return ResponseEntity.ok(factorService.getFactorEnumRule(ruleId));
    }

    @PutMapping("/enum-rules/{ruleId}")
    public ResponseEntity<FactorEnumRuleResponse> updateFactorEnumRule(
            @PathVariable Long ruleId,
            @Valid @RequestBody UpdateFactorEnumRuleRequest request
    ) {
        return ResponseEntity.ok(factorService.updateFactorEnumRule(ruleId, request));
    }

    @DeleteMapping("/enum-rules/{ruleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFactorEnumRule(@PathVariable Long ruleId) {
        factorService.deleteFactorEnumRule(ruleId);
    }
}