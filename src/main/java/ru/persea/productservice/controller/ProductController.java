package ru.persea.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.CategoryDto;
import ru.persea.productservice.dto.ProductDto;
import ru.persea.productservice.dto.ProductInclude;
import ru.persea.productservice.service.ProductService;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getMethodName(
        @RequestParam(value = "category_id", required = false) Integer categoryId,
        @RequestParam(value = "brand_ids", required = false) Set<Integer> brandsIds,
        @RequestParam(value = "min_rating", required = false) Integer minRating,
        @RequestParam(value = "max_rating", required = false) Integer maxRating,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            productService.getProducts(
                categoryId, 
                brandsIds, 
                minRating, 
                maxRating, 
                pageable
            )
        );
    }
    
    @GetMapping("/categories")
    public ResponseEntity<Set<CategoryDto>> getCategories() {
        return ResponseEntity.ok(productService.getCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(
        @PathVariable("id") Long id,
        @RequestParam(value = "include", required = false) Set<ProductInclude> includes
    ) {
        return ResponseEntity.ok(productService.getProduct(id, includes));
    }
}