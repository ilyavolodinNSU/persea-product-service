package ru.persea.productservice.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;

import ru.persea.productservice.dto.product.brand.request.CreateBrandRequest;
import ru.persea.productservice.dto.product.brand.request.UpdateBrandRequest;
import ru.persea.productservice.dto.product.brand.response.BrandDto;
import ru.persea.productservice.dto.product.category.request.CreateCategoryRequest;
import ru.persea.productservice.dto.product.category.request.UpdateCategoryRequest;
import ru.persea.productservice.dto.product.category.response.CategoryDto;
import ru.persea.productservice.dto.product.product.ProductInclude;
import ru.persea.productservice.dto.product.product.ProductSearchDto;
import ru.persea.productservice.dto.product.product.request.CreateProductRequest;
import ru.persea.productservice.dto.product.product.request.UpdateProductRequest;
import ru.persea.productservice.dto.product.product.response.ProductResponse;

public interface ProductService {
    // categories
    CategoryDto createCategory(CreateCategoryRequest request);
    List<CategoryDto> getCategories();
    CategoryDto getCategory(Long id);
    CategoryDto updateCategory(Long id, UpdateCategoryRequest request);
    void deleteCategory(Long id);

    // brands
    BrandDto createBrand(CreateBrandRequest request);
    List<BrandDto> getBrands();
    BrandDto getBrand(Long id);
    BrandDto updateBrand(Long id, UpdateBrandRequest request);
    void deleteBrand(Long id);

    // products
    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse getProduct(Long id, Set<ProductInclude> includes);
    ProductResponse updateProduct(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);

    public List<ProductSearchDto> searchProducts(String query, Integer categoryId, Set<Integer> brandsIds, Integer minRating, Integer maxRating, Pageable pageable);

    public List<String> getSuggestions(String query, int limit);
}
