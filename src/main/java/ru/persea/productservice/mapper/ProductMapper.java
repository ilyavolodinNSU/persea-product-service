package ru.persea.productservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.persea.productservice.dto.product.brand.response.BrandDto;
import ru.persea.productservice.dto.product.category.response.CategoryDto;
import ru.persea.productservice.dto.product.product.response.ProductBooleanFactorResponse;
import ru.persea.productservice.dto.product.product.response.ProductEnumFactorResponse;
import ru.persea.productservice.dto.product.product.response.ProductNumericFactorResponse;
import ru.persea.productservice.dto.product.product.response.ProductResponse;
import ru.persea.productservice.entity.product.BrandEntity;
import ru.persea.productservice.entity.product.CategoryEntity;
import ru.persea.productservice.entity.product.ProductEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    CategoryDto toDto(CategoryEntity entity);

    BrandDto toDto(BrandEntity entity);

    ProductResponse toDto(ProductEntity entity);

    @Mapping(target = "numericFactors", source = "numFactors")
    @Mapping(target = "booleanFactors", source = "boolFactors")
    @Mapping(target = "enumFactors", source = "enumFactors")
    ProductResponse toDto(
        ProductEntity entity, 
        List<ProductNumericFactorResponse> numFactors, 
        List<ProductBooleanFactorResponse> boolFactors, 
        List<ProductEnumFactorResponse> enumFactors
    );
}