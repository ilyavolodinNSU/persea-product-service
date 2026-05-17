package ru.persea.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.product.BrandDto;
import ru.persea.productservice.dto.product.CategoryDto;
import ru.persea.productservice.dto.product.ProductInclude;
import ru.persea.productservice.dto.product.ProductSearchDto;
import ru.persea.productservice.dto.product.request.CreateCategory;
import ru.persea.productservice.dto.product.request.CreateProductRequest;
import ru.persea.productservice.dto.product.response.ProductResponse;
import ru.persea.productservice.service.ProductService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategory request) {
        return ResponseEntity.ok(productService.createCategory(request));
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(productService.getCategories());
    }

    @PostMapping("/brands")
    public ResponseEntity<BrandDto> createBrand(@RequestBody String name) {
        return ResponseEntity.ok(productService.createBrand(name));
    }

    @GetMapping("/brands")
    public ResponseEntity<List<BrandDto>> getBrands() {
        return ResponseEntity.ok(productService.getBrands());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
        @RequestBody CreateProductRequest request
    ) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
        @PathVariable("id") Long id,
        @RequestParam(value = "include", required = false) Set<ProductInclude> includes
    ) {
        return ResponseEntity.ok(productService.getProduct(id, includes));
    }
}