package ru.persea.productservice.service;

import java.util.List;
import java.util.Set;

import ru.persea.productservice.dto.ProductDto;
import ru.persea.productservice.dto.ProductInclude;

public interface ProductService {
    public ProductDto getProduct(Long id, Set<ProductInclude> includes);
}
