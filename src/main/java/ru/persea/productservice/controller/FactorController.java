package ru.persea.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.factor.FactorDto;
import ru.persea.productservice.dto.factor.FactorTypeDto;
import ru.persea.productservice.dto.factor.UnitDto;
import ru.persea.productservice.dto.factor.request.CreateFactorBooleanRuleRequest;
import ru.persea.productservice.dto.factor.request.CreateFactorEnumRuleRequest;
import ru.persea.productservice.dto.factor.request.CreateFactorEnumValueRequest;
import ru.persea.productservice.dto.factor.request.CreateFactorNumericRuleRequest;
import ru.persea.productservice.dto.factor.request.CreateFactorRequest;
import ru.persea.productservice.dto.factor.response.FactorBooleanRuleResponse;
import ru.persea.productservice.dto.factor.response.FactorEnumRuleResponse;
import ru.persea.productservice.dto.factor.response.FactorEnumValueResponse;
import ru.persea.productservice.dto.factor.response.FactorNumericRuleResponse;
import ru.persea.productservice.service.FactorService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/factors")
@RequiredArgsConstructor
public class FactorController {
    private final FactorService factorService;

    @PostMapping("/units")
    public ResponseEntity<UnitDto> createUnit(@RequestBody String name) {
        return ResponseEntity.ok(factorService.createUnit(name));
    }

    @GetMapping("/units")
    public ResponseEntity<List<UnitDto>> getUnits() {
        return ResponseEntity.ok(factorService.getUnits());
    }

    @PostMapping("/types")
    public ResponseEntity<FactorTypeDto> createFactorType(String name) {
        return ResponseEntity.ok(factorService.createFactorType(name));
    }

    @GetMapping("/types")
    public ResponseEntity<List<FactorTypeDto>> getFactorTypes() {
        return ResponseEntity.ok(factorService.getFactorTypes());
    }

    @PostMapping
    public ResponseEntity<FactorDto> createFactor(CreateFactorRequest request) {
        return ResponseEntity.ok(factorService.createFactor(request));
    }

    @GetMapping
    public ResponseEntity<List<FactorDto>> getFactors() {
        return ResponseEntity.ok(factorService.getFactors());
    }

    @PostMapping("/{factorId}/numeric-rules")
    public ResponseEntity<FactorNumericRuleResponse> createFactorNumericRule(
        @PathVariable("factorId") Long factorId,
        CreateFactorNumericRuleRequest request
    ) {
        return ResponseEntity.ok(factorService.createFactorNumericRule(factorId, request));
    }

    @PostMapping("/{factorId}/boolean-rules")
    public ResponseEntity<FactorBooleanRuleResponse> createFactorBooleanRule(
        @PathVariable("factorId") Long factorId,
        CreateFactorBooleanRuleRequest request
    ) {
        return ResponseEntity.ok(factorService.createFactorBooleanRule(factorId, request));
    }

    @PostMapping("/{factorId}/enum-values")
    public ResponseEntity<FactorEnumValueResponse> createFactorEnumValue(
        @PathVariable("factorId") Long factorId,
        CreateFactorEnumValueRequest request
    ) {
        return ResponseEntity.ok(factorService.createFactorEnumValue(factorId, request));
    }

    @PostMapping("/{factorId}/enum-values/{valueId}/enum-rules")
    public ResponseEntity<FactorEnumRuleResponse> createFactorEnumRule(
        CreateFactorEnumRuleRequest request
    ) {
        return ResponseEntity.ok(factorService.createFactorEnumRule(request));
    }
}