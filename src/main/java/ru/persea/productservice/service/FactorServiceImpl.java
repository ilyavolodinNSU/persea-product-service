package ru.persea.productservice.service;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

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
import ru.persea.productservice.entity.factor.FactorBooleanRuleEntity;
import ru.persea.productservice.entity.factor.FactorEntity;
import ru.persea.productservice.entity.factor.FactorEnumRuleEntity;
import ru.persea.productservice.entity.factor.FactorEnumValueEntity;
import ru.persea.productservice.entity.factor.FactorNumericRuleEntity;
import ru.persea.productservice.entity.factor.FactorTypeEntity;
import ru.persea.productservice.entity.factor.UnitEntity;
import ru.persea.productservice.mapper.FactorMapper;
import ru.persea.productservice.repository.product.CategoryRepository;
import ru.persea.productservice.repository.factor.FactorBooleanRuleRepository;
import ru.persea.productservice.repository.factor.FactorEnumRuleRepository;
import ru.persea.productservice.repository.factor.FactorEnumValueRepository;
import ru.persea.productservice.repository.factor.FactorNumericRuleRepository;
import ru.persea.productservice.repository.factor.FactorRepository;
import ru.persea.productservice.repository.factor.FactorTypeRepository;
import ru.persea.productservice.repository.factor.UnitRepository;

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

    private UnitEntity getUnitEntity(Short id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Единица измерения не найдена: " + id));
    }

    private FactorTypeEntity getFactorTypeEntity(Short id) {
        return factorTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Тип фактора не найден: " + id));
    }

    private FactorEntity getFactorEntity(Long id) {
        return factorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Фактор не найден: " + id));
    }

    private FactorNumericRuleEntity getNumericRuleEntity(Long id) {
        return factorNumericRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Numeric правило не найдено: " + id));
    }

    private FactorBooleanRuleEntity getBooleanRuleEntity(Long id) {
        return factorBooleanRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Boolean правило не найдено: " + id));
    }

    private FactorEnumValueEntity getEnumValueEntity(Long id) {
        return factorEnumValueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Enum значение не найдено: " + id));
    }

    private FactorEnumRuleEntity getEnumRuleEntity(Long id) {
        return factorEnumRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Enum правило не найдено: " + id));
    }

    // units

    @Override
    public UnitDto createUnit(CreateUnitRequest request) {
        var unit = new UnitEntity();
        unit.setName(request.name());

        return factorMapper.toDto(unitRepository.save(unit));
    }

    @Override
    public List<UnitDto> getUnits() {
        return unitRepository.findAll().stream()
                .map(factorMapper::toDto)
                .toList();
    }

    @Override
    public UnitDto getUnit(Short id) {
        return factorMapper.toDto(getUnitEntity(id));
    }

    @Override
    public UnitDto updateUnit(Short id, UpdateUnitRequest request) {
        var unit = getUnitEntity(id);
        unit.setName(request.name());
        return factorMapper.toDto(unitRepository.save(unit));
    }

    @Override
    public void deleteUnit(Short id) {
        unitRepository.deleteById(id);
    }

    // factor types

    @Override
    public FactorTypeDto createFactorType(CreateFactorTypeRequest request) {
        var factorType = new FactorTypeEntity();
        factorType.setName(request.name());

        return factorMapper.toDto(factorTypeRepository.save(factorType));
    }

    @Override
    public List<FactorTypeDto> getFactorTypes() {
        return factorTypeRepository.findAll().stream()
                .map(factorMapper::toDto)
                .toList();
    }

    @Override
    public FactorTypeDto getFactorType(Short id) {
        return factorMapper.toDto(getFactorTypeEntity(id));
    }

    @Override
    public FactorTypeDto updateFactorType(Short id, UpdateFactorTypeRequest request) {
        var factorType = getFactorTypeEntity(id);
        factorType.setName(request.name());
        return factorMapper.toDto(factorTypeRepository.save(factorType));
    }

    @Override
    public void deleteFactorType(Short id) {
        factorTypeRepository.deleteById(id);
    }

    // factors

    @Override
    public FactorDto createFactor(CreateFactorRequest request) {
        var factor = new FactorEntity();
        factor.setName(request.name());
        factor.setType(getFactorTypeEntity(request.typeId()));
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
    public FactorDto getFactor(Long id) {
        return factorMapper.toDto(getFactorEntity(id));
    }

    @Override
    public FactorDto updateFactor(Long id, UpdateFactorRequest request) {
        var factor = getFactorEntity(id);
        factor.setName(request.name());
        factor.setType(getFactorTypeEntity(request.typeId()));
        factor.setDescription(request.description());

        return factorMapper.toDto(factorRepository.save(factor));
    }

    @Override
    public void deleteFactor(Long id) {
        factorRepository.deleteById(id);
    }

    // numeric rules

    @Override
    public FactorNumericRuleResponse createFactorNumericRule(
        Long factorId,
        CreateFactorNumericRuleRequest request
    ) {
        var numericRule = new FactorNumericRuleEntity();
        numericRule.setCategory(categorysRepository.getReferenceById(request.categoryId()));
        numericRule.setFactor(factorRepository.getReferenceById(factorId));
        numericRule.setUnit(unitRepository.getReferenceById(request.unitId()));
        numericRule.setMinValue(request.minValue());
        numericRule.setMaxValue(request.maxValue());

        return factorMapper.toDto(factorNumericRuleRepository.save(numericRule));
    }

    @Override
    public FactorNumericRuleResponse getFactorNumericRule(Long id) {
        return factorMapper.toDto(getNumericRuleEntity(id));
    }

    @Override
    public FactorNumericRuleResponse updateFactorNumericRule(Long id, UpdateFactorNumericRuleRequest request) {
        var numericRule = getNumericRuleEntity(id);
        numericRule.setCategory(categorysRepository.getReferenceById(request.categoryId()));
        numericRule.setUnit(unitRepository.getReferenceById(request.unitId()));
        numericRule.setMinValue(request.minValue());
        numericRule.setMaxValue(request.maxValue());

        return factorMapper.toDto(factorNumericRuleRepository.save(numericRule));
    }

    @Override
    public void deleteFactorNumericRule(Long id) {
        factorNumericRuleRepository.deleteById(id);
    }

    // boolean rules

    @Override
    public FactorBooleanRuleResponse createFactorBooleanRule(
        Long factorId,
        CreateFactorBooleanRuleRequest request
    ) {
        var booleanRule = new FactorBooleanRuleEntity();
        booleanRule.setCategory(categorysRepository.getReferenceById(request.categoryId()));
        booleanRule.setFactor(factorRepository.getReferenceById(factorId));
        booleanRule.setImpact(request.impact());

        return factorMapper.toDto(factorBooleanRuleRepository.save(booleanRule));
    }

    @Override
    public FactorBooleanRuleResponse getFactorBooleanRule(Long id) {
        return factorMapper.toDto(getBooleanRuleEntity(id));
    }

    @Override
    public FactorBooleanRuleResponse updateFactorBooleanRule(Long id, UpdateFactorBooleanRuleRequest request) {
        var booleanRule = getBooleanRuleEntity(id);
        booleanRule.setCategory(categorysRepository.getReferenceById(request.categoryId()));
        booleanRule.setImpact(request.impact());

        return factorMapper.toDto(factorBooleanRuleRepository.save(booleanRule));
    }

    @Override
    public void deleteFactorBooleanRule(Long id) {
        factorBooleanRuleRepository.deleteById(id);
    }

    // enum values

    @Override
    public FactorEnumValueResponse createFactorEnumValue(
        Long factorId,
        CreateFactorEnumValueRequest request
    ) {
        var enumValue = new FactorEnumValueEntity();
        enumValue.setFactor(factorRepository.getReferenceById(factorId));
        enumValue.setValue(request.value());

        return factorMapper.toDto(factorEnumValueRepository.save(enumValue));
    }

    @Override
    public FactorEnumValueResponse getFactorEnumValue(Long id) {
        return factorMapper.toDto(getEnumValueEntity(id));
    }

    @Override
    public FactorEnumValueResponse updateFactorEnumValue(Long id, UpdateFactorEnumValueRequest request) {
        var enumValue = getEnumValueEntity(id);
        enumValue.setValue(request.value());

        return factorMapper.toDto(factorEnumValueRepository.save(enumValue));
    }

    @Override
    public void deleteFactorEnumValue(Long id) {
        factorEnumValueRepository.deleteById(id);
    }

    // enum rules

    @Override
    public FactorEnumRuleResponse createFactorEnumRule(
        Long valueId,
        CreateFactorEnumRuleRequest request
    ) {
        var enumRule = new FactorEnumRuleEntity();
        enumRule.setCategory(categorysRepository.getReferenceById(request.categoryId()));
        enumRule.setEnumValue(factorEnumValueRepository.getReferenceById(valueId));
        enumRule.setImpact(request.impact());

        return factorMapper.toDto(factorEnumRuleRepository.save(enumRule));
    }

    @Override
    public FactorEnumRuleResponse getFactorEnumRule(Long id) {
        return factorMapper.toDto(getEnumRuleEntity(id));
    }

    @Override
    public FactorEnumRuleResponse updateFactorEnumRule(Long id, UpdateFactorEnumRuleRequest request) {
        var enumRule = new FactorEnumRuleEntity();
        enumRule.setCategory(categorysRepository.getReferenceById(request.categoryId()));
        enumRule.setEnumValue(factorEnumValueRepository.getReferenceById(request.valueId()));
        enumRule.setImpact(request.impact());

        return factorMapper.toDto(factorEnumRuleRepository.save(enumRule));
    }

    @Override
    public void deleteFactorEnumRule(Long id) {
        factorEnumRuleRepository.deleteById(id);
    }
}
