package ru.persea.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
import ru.persea.productservice.entity.factor.FactorBooleanRuleEntity;
import ru.persea.productservice.entity.factor.FactorEntity;
import ru.persea.productservice.entity.factor.FactorEnumRuleEntity;
import ru.persea.productservice.entity.factor.FactorEnumValueEntity;
import ru.persea.productservice.entity.factor.FactorNumericRuleEntity;
import ru.persea.productservice.entity.factor.FactorTypeEntity;
import ru.persea.productservice.entity.factor.UnitEntity;
import ru.persea.productservice.mapper.FactorMapper;
import ru.persea.productservice.repository.CategoryRepository;
import ru.persea.productservice.repository.FactorBooleanRuleRepository;
import ru.persea.productservice.repository.FactorEnumRuleRepository;
import ru.persea.productservice.repository.FactorEnumValueRepository;
import ru.persea.productservice.repository.FactorNumericRuleRepository;
import ru.persea.productservice.repository.FactorRepository;
import ru.persea.productservice.repository.FactorTypeRepository;
import ru.persea.productservice.repository.UnitRepository;

@Service
@RequiredArgsConstructor
public class FactorServiceImpl implements FactorService {
    private final CategoryRepository categorysRepository;
    private final UnitRepository unitRepository;
    private final FactorTypeRepository factorTypeRepository;
    private final FactorRepository factorRepository;
    private final FactorNumericRuleRepository factorNumericRuleRepository;
    private final FactorBooleanRuleRepository factorBooleanRuleRepository;
    private final FactorEnumValueRepository factorEnumValueRepository;
    private final FactorEnumRuleRepository factorEnumRuleRepository;


    private final FactorMapper factorMapper;

    @Override
    public UnitDto createUnit(String name) {
        var unit = new UnitEntity();
        unit.setName(name);

        return factorMapper.toDto(unitRepository.save(unit));
    }

    @Override
    public List<UnitDto> getUnits() {
        return unitRepository.findAll().stream()
                .map(factorMapper::toDto)
                .toList();
    }

    @Override
    public FactorTypeDto createFactorType(String name) {
        var factorType = new FactorTypeEntity();
        factorType.setName(name);

        return factorMapper.toDto(factorTypeRepository.save(factorType));
    }

    @Override
    public List<FactorTypeDto> getFactorTypes() {
        return factorTypeRepository.findAll().stream()
                .map(factorMapper::toDto)
                .toList();
    }

    @Override
    public FactorDto createFactor(CreateFactorRequest request) {
        var factor = new FactorEntity();
        factor.setName(request.name());
        factor.setType(factorTypeRepository.getReferenceById(request.typeId()));
        factor.setDescription(request.description());

        return factorMapper.toDto(factorRepository.save(factor));
    }

    @Override
    public List<FactorDto> getFactors() {
        return factorRepository.findAll().stream()
                .map(factorMapper::toDto)
                .toList();
    }

    @Override
    public FactorNumericRuleResponse createFactorNumericRule(CreateFactorNumericRuleRequest request) {
        var numericRule = new FactorNumericRuleEntity();
        numericRule.setCategory(categorysRepository.getReferenceById(request.categoryId()));
        numericRule.setFactor(factorRepository.getReferenceById(request.factorId()));
        numericRule.setUnit(unitRepository.getReferenceById(request.unitId()));
        numericRule.setMinValue(request.minValue());
        numericRule.setMaxValue(request.maxValue());

        return factorMapper.toDto(factorNumericRuleRepository.save(numericRule));
    }

    @Override
    public FactorBooleanRuleResponse createFactorBooleanRule(CreateFactorBooleanRuleRequest request) {
        var booleanRule = new FactorBooleanRuleEntity();
        booleanRule.setCategory(categorysRepository.getReferenceById(request.categoryId()));
        booleanRule.setFactor(factorRepository.getReferenceById(request.factorId()));
        booleanRule.setImpact(request.impact());

        return factorMapper.toDto(factorBooleanRuleRepository.save(booleanRule));
    }

    @Override
    public FactorEnumValueResponse createFactorEnumValue(CreateFactorEnumValueRequest request) {
        var enumValue = new FactorEnumValueEntity();
        enumValue.setFactor(factorRepository.getReferenceById(request.factorId()));
        enumValue.setValue(request.value());

        return factorMapper.toDto(factorEnumValueRepository.save(enumValue));
    }

    @Override
    public FactorEnumRuleResponse createFactorEnumRule(CreateFactorEnumRuleRequest request) {
        var enumRule = new FactorEnumRuleEntity();
        enumRule.setCategory(categorysRepository.getReferenceById(request.categoryId()));
        enumRule.setEnumValue(factorEnumValueRepository.getReferenceById(request.valueId()));
        enumRule.setImpact(request.impact());

        return factorMapper.toDto(factorEnumRuleRepository.save(enumRule));
    }
}
