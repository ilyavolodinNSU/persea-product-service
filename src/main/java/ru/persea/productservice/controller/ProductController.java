package ru.persea.productservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.product.brand.request.CreateBrandRequest;
import ru.persea.productservice.dto.product.brand.request.UpdateBrandRequest;
import ru.persea.productservice.dto.product.brand.response.BrandDto;
import ru.persea.productservice.dto.product.category.request.CreateCategoryRequest;
import ru.persea.productservice.dto.product.category.request.UpdateCategoryRequest;
import ru.persea.productservice.dto.product.category.response.CategoryDto;
import ru.persea.productservice.dto.product.product.ProductInclude;
import ru.persea.productservice.dto.product.product.request.CreateProductRequest;
import ru.persea.productservice.dto.product.product.request.UpdateProductRequest;
import ru.persea.productservice.dto.product.product.response.ProductResponse;
import ru.persea.productservice.service.ProductService;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // categories

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CreateCategoryRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createCategory(request));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(productService.getCategories());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getCategory(id));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody UpdateCategoryRequest request
    ) {
        return ResponseEntity.ok(productService.updateCategory(id, request));
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        productService.deleteCategory(id);
    }

    // brands

    @PostMapping("/brands")
    public ResponseEntity<BrandDto> createBrand(@RequestBody CreateBrandRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createBrand(request));
    }

    @GetMapping("/brands")
    public ResponseEntity<List<BrandDto>> getBrands() {
        return ResponseEntity.ok(productService.getBrands());
    }

    @GetMapping("/brands/{id}")
    public ResponseEntity<BrandDto> getBrand(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getBrand(id));
    }

    @PutMapping("/brands/{id}")
    public ResponseEntity<BrandDto> updateBrand(
            @PathVariable Long id,
            @RequestBody UpdateBrandRequest request
    ) {
        return ResponseEntity.ok(productService.updateBrand(id, request));
    }

    @DeleteMapping("/brands/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBrand(@PathVariable Long id) {
        productService.deleteBrand(id);
    }

    // products

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody CreateProductRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable Long id,
            @RequestParam(value = "include", required = false) Set<ProductInclude> includes
    ) {
        return ResponseEntity.ok(productService.getProduct(id, includes));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest request
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}