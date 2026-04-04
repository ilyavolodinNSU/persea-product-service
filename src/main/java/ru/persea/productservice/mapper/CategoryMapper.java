package ru.persea.productservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import ru.persea.productservice.dto.CategoryDto;
import ru.persea.productservice.entity.CategoryEntity;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CategoryMapper {
    public CategoryDto toDto(CategoryEntity entity);
}
