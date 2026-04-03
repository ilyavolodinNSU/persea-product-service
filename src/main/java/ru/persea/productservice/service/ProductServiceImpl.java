package ru.persea.productservice.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.FactorDto;
import ru.persea.productservice.dto.ProductDto;
import ru.persea.productservice.dto.ProductInclude;
import ru.persea.productservice.entity.ProductEntity;
import ru.persea.productservice.entity.ProductFactorEntity;
import ru.persea.productservice.mapper.ProductFactorMapper;
import ru.persea.productservice.mapper.ProductMapper;
import ru.persea.productservice.repository.ProductFactorsRepository;
import ru.persea.productservice.repository.ProductsRepository;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository productsRepository;
    private final ProductFactorsRepository productFactorsRepository;
    
    private final ProductMapper productMapper;

    @Override
    public ProductDto getProduct(Long id, Set<ProductInclude> includes) {
        ProductEntity entity = productsRepository.findById(id).orElseThrow();
        List<ProductFactorEntity> factors = null;

        if (includes != null && includes.contains(ProductInclude.FACTORS))
            factors = productFactorsRepository.findByProductId(id);

        return productMapper.toDto(entity, factors);
    }
}
