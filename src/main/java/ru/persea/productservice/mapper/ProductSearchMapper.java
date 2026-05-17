package ru.persea.productservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import ru.persea.productservice.dto.product.ProductSearchDto;
import ru.persea.productservice.entity.product.ProductDocument;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductSearchMapper {
    ProductSearchDto toDto(ProductDocument document);
}