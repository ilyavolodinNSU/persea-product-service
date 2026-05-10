package ru.persea.productservice.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import ru.persea.productservice.dto.product.BrandDto;
import ru.persea.productservice.dto.product.CategoryDto;
import ru.persea.productservice.dto.product.ProductInclude;
import ru.persea.productservice.dto.product.ProductSearchDto;
import ru.persea.productservice.dto.product.request.CreateProductRequest;
import ru.persea.productservice.dto.product.response.ProductResponse;

public interface ProductService {
    public CategoryDto createCategory(String name);

    public Set<CategoryDto> getCategories();

    public BrandDto createBrand(String name);

    public List<BrandDto> getBrands();

    public ProductResponse createProduct(CreateProductRequest request);

    public ProductResponse getProduct(Long id, Set<ProductInclude> includes);

    public List<ProductSearchDto> searchProducts(String query, Integer categoryId, Set<Integer> brandsIds, Integer minRating, Integer maxRating, Pageable pageable);

    public List<String> getSuggestions(String query, int limit);
}
