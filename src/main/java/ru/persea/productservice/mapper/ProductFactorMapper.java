package ru.persea.productservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import ru.persea.productservice.dto.FactorDto;
import ru.persea.productservice.entity.ProductFactorEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductFactorMapper {

    @Mapping(target = "name", source = "factor.name")
    @Mapping(target = "type", source = "factor.type.name")
    @Mapping(target = "unit", source = "factor.unit.name")
    @Mapping(target = "amount", source = "amount")
    public FactorDto toDto(ProductFactorEntity entity);

    public List<FactorDto> toDtoList(List<ProductFactorEntity> entities);
}
