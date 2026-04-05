package ru.persea.productservice.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.CategoryDto;
import ru.persea.productservice.dto.FactorDto;
import ru.persea.productservice.dto.ProductDto;
import ru.persea.productservice.dto.ProductInclude;
import ru.persea.productservice.entity.ProductEntity;
import ru.persea.productservice.entity.ProductFactorEntity;
import ru.persea.productservice.mapper.CategoryMapper;
import ru.persea.productservice.mapper.ProductFactorMapper;
import ru.persea.productservice.mapper.ProductMapper;
import ru.persea.productservice.repository.CategoriesRepository;
import ru.persea.productservice.repository.ProductFactorsRepository;
import ru.persea.productservice.repository.ProductsRepository;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository productsRepository;
    private final ProductFactorsRepository productFactorsRepository;
    private final CategoriesRepository categoriesRepository;
    
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public ProductDto getProduct(Long id, Set<ProductInclude> includes) {
        ProductEntity entity = productsRepository.findById(id).orElseThrow();
        List<ProductFactorEntity> factors = null;

        if (includes != null && includes.contains(ProductInclude.FACTORS))
            factors = productFactorsRepository.findByProductId(id);

        return productMapper.toDto(entity, factors);
    }

    @Override
    public Set<CategoryDto> getCategories() {
        return categoriesRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public List<ProductDto> getProducts(
        Integer categoryId, 
        Set<Integer> brandsIds, 
        Integer minRating, 
        Integer maxRating, 
        Pageable pageable
    ) {
        Specification<ProductEntity> spec = Specification
            .where(ProductSpecification.hasCategoryId(categoryId))
            .and(ProductSpecification.hasBrandIds(brandsIds))
            .and(ProductSpecification.ratingBetween(minRating, maxRating));

        return productsRepository.findAll(spec, pageable).stream()
                    .map(productMapper::toDto)
                    .toList();
    }
}
