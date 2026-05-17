package ru.persea.productservice.service;

import java.util.List;

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

public interface FactorService {
    public UnitDto createUnit(String name);

    public List<UnitDto> getUnits();

    public FactorTypeDto createFactorType(String name);

    public List<FactorTypeDto> getFactorTypes();

    public FactorDto createFactor(CreateFactorRequest request);

    public List<FactorDto> getFactors();

    public FactorNumericRuleResponse createFactorNumericRule(Long factorId, CreateFactorNumericRuleRequest request);

    public FactorBooleanRuleResponse createFactorBooleanRule(Long factorId, CreateFactorBooleanRuleRequest request);

    public FactorEnumValueResponse createFactorEnumValue(Long factorId, CreateFactorEnumValueRequest request);

    public FactorEnumRuleResponse createFactorEnumRule(CreateFactorEnumRuleRequest request);
}
