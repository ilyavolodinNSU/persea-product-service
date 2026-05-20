package ru.persea.productservice.service;

import java.util.List;

import ru.persea.productservice.dto.factor.factor.response.FactorDto;
import ru.persea.productservice.dto.factor.factorType.response.FactorTypeDto;
import ru.persea.productservice.dto.factor.unit.response.UnitDto;
import ru.persea.productservice.dto.factor.factor.request.CreateFactorBooleanRuleRequest;
import ru.persea.productservice.dto.factor.factor.request.CreateFactorEnumRuleRequest;
import ru.persea.productservice.dto.factor.factor.request.CreateFactorEnumValueRequest;
import ru.persea.productservice.dto.factor.factor.request.CreateFactorNumericRuleRequest;
import ru.persea.productservice.dto.factor.factor.request.CreateFactorRequest;
import ru.persea.productservice.dto.factor.factorType.request.CreateFactorTypeRequest;
import ru.persea.productservice.dto.factor.unit.request.CreateUnitRequest;
import ru.persea.productservice.dto.factor.factor.request.UpdateFactorBooleanRuleRequest;
import ru.persea.productservice.dto.factor.factor.request.UpdateFactorEnumRuleRequest;
import ru.persea.productservice.dto.factor.factor.request.UpdateFactorEnumValueRequest;
import ru.persea.productservice.dto.factor.factor.request.UpdateFactorNumericRuleRequest;
import ru.persea.productservice.dto.factor.factor.request.UpdateFactorRequest;
import ru.persea.productservice.dto.factor.factorType.request.UpdateFactorTypeRequest;
import ru.persea.productservice.dto.factor.unit.request.UpdateUnitRequest;
import ru.persea.productservice.dto.factor.factor.response.FactorBooleanRuleResponse;
import ru.persea.productservice.dto.factor.factor.response.FactorEnumRuleResponse;
import ru.persea.productservice.dto.factor.factor.response.FactorEnumValueResponse;
import ru.persea.productservice.dto.factor.factor.response.FactorNumericRuleResponse;

public interface FactorService {

    // units
    UnitDto createUnit(CreateUnitRequest request);
    List<UnitDto> getUnits();
    UnitDto getUnit(Short id);
    UnitDto updateUnit(Short id, UpdateUnitRequest request);
    void deleteUnit(Short id);

    // factor types
    FactorTypeDto createFactorType(CreateFactorTypeRequest request);
    List<FactorTypeDto> getFactorTypes();
    FactorTypeDto getFactorType(Short id);
    FactorTypeDto updateFactorType(Short id, UpdateFactorTypeRequest request);
    void deleteFactorType(Short id);

    // factors
    FactorDto createFactor(CreateFactorRequest request);
    List<FactorDto> getFactors();
    FactorDto getFactor(Long id);
    FactorDto updateFactor(Long id, UpdateFactorRequest request);
    void deleteFactor(Long id);

    // numeric rules
    FactorNumericRuleResponse createFactorNumericRule(Long factorId, CreateFactorNumericRuleRequest request);
    FactorNumericRuleResponse getFactorNumericRule(Long id);
    FactorNumericRuleResponse updateFactorNumericRule(Long id, UpdateFactorNumericRuleRequest request);
    void deleteFactorNumericRule(Long id);

    // boolean rules
    FactorBooleanRuleResponse createFactorBooleanRule(Long factorId, CreateFactorBooleanRuleRequest request);
    FactorBooleanRuleResponse getFactorBooleanRule(Long id);
    FactorBooleanRuleResponse updateFactorBooleanRule(Long id, UpdateFactorBooleanRuleRequest request);
    void deleteFactorBooleanRule(Long id);

    // enum values
    FactorEnumValueResponse createFactorEnumValue(Long factorId, CreateFactorEnumValueRequest request);
    FactorEnumValueResponse getFactorEnumValue(Long id);
    FactorEnumValueResponse updateFactorEnumValue(Long id, UpdateFactorEnumValueRequest request);
    void deleteFactorEnumValue(Long id);

    // enum rules
    FactorEnumRuleResponse createFactorEnumRule(Long valueId, CreateFactorEnumRuleRequest request);
    FactorEnumRuleResponse getFactorEnumRule(Long id);
    FactorEnumRuleResponse updateFactorEnumRule(Long id, UpdateFactorEnumRuleRequest request);
    void deleteFactorEnumRule(Long id);
}
