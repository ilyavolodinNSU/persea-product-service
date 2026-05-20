package ru.persea.productservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import ru.persea.productservice.dto.factor.factor.response.FactorDto;
import ru.persea.productservice.dto.factor.factorType.response.FactorTypeDto;
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

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FactorMapper {
    public UnitDto toDto(UnitEntity entity);

    public FactorTypeDto toDto(FactorTypeEntity entity);

    public FactorDto toDto(FactorEntity entity);

    public FactorNumericRuleResponse toDto(FactorNumericRuleEntity entity);

    public FactorBooleanRuleResponse toDto(FactorBooleanRuleEntity entity);

    public FactorEnumValueResponse toDto(FactorEnumValueEntity entity);

    public FactorEnumRuleResponse toDto(FactorEnumRuleEntity entity);
}
