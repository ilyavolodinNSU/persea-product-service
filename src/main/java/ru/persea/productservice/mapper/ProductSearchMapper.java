package ru.persea.productservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.persea.productservice.dto.ProductSearchDto;
import ru.persea.productservice.entity.ProductDocument;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = ProductFactorMapper.class
)
public interface ProductSearchMapper {
    ProductSearchDto toDto(ProductDocument document);
}
