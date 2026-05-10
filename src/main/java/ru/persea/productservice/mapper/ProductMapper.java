package ru.persea.productservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.persea.productservice.dto.product.BrandDto;
import ru.persea.productservice.dto.product.CategoryDto;
import ru.persea.productservice.dto.product.response.ProductBooleanFactorResponse;
import ru.persea.productservice.dto.product.response.ProductEnumFactorResponse;
import ru.persea.productservice.dto.product.response.ProductNumericFactorResponse;
import ru.persea.productservice.dto.product.response.ProductResponse;
import ru.persea.productservice.entity.product.BrandEntity;
import ru.persea.productservice.entity.product.CategoryEntity;
import ru.persea.productservice.entity.product.ProductBooleanFactorEntity;
import ru.persea.productservice.entity.product.ProductEntity;
import ru.persea.productservice.entity.product.ProductEnumFactorEntity;
import ru.persea.productservice.entity.product.ProductNumericFactorEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    CategoryDto toDto(CategoryEntity entity);

    BrandDto toDto(BrandEntity entity);

    ProductResponse toDto(ProductEntity entity);

    @Mapping(target = "factorId", source = "factor.id")
    @Mapping(target = "factorName", source = "factor.name")
    ProductNumericFactorResponse toDto(ProductNumericFactorEntity entity);

    @Mapping(target = "factorId", source = "factor.id")
    @Mapping(target = "factorName", source = "factor.name")
    ProductBooleanFactorResponse toDto(ProductBooleanFactorEntity entity);

    @Mapping(target = "factorId", source = "factor.id")
    @Mapping(target = "factorName", source = "factor.name")
    @Mapping(target = "enumValue", source = "enumValue.value")
    ProductEnumFactorResponse toDto(ProductEnumFactorEntity entity);

    ProductResponse toDto(
        ProductEntity entity, 
        List<ProductNumericFactorEntity> numFactors, 
        List<ProductBooleanFactorEntity> boolFactors, 
        List<ProductEnumFactorEntity> enumFactors
    );
}