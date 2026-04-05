package ru.persea.productservice.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;

import ru.persea.productservice.dto.CategoryDto;
import ru.persea.productservice.dto.ProductDto;
import ru.persea.productservice.dto.ProductInclude;

public interface ProductService {
    public ProductDto getProduct(Long id, Set<ProductInclude> includes);

    public Set<CategoryDto> getCategories();

    public List<ProductDto> getProducts(Integer categoryId, Set<Integer> brandsIds, Integer minRating, Integer maxRating, Pageable pageable);
}
