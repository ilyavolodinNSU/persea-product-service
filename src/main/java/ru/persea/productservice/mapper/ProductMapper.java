package ru.persea.productservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import ru.persea.productservice.dto.FactorDto;
import ru.persea.productservice.dto.ProductDto;
import ru.persea.productservice.entity.ProductEntity;
import ru.persea.productservice.entity.ProductFactorEntity;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = ProductFactorMapper.class
)
public interface ProductMapper {

    @Mapping(target = "factors", source = "factorEntities")
    ProductDto toDto(ProductEntity productEntity, List<ProductFactorEntity> factorEntities);
}
