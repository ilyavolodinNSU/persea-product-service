package ru.persea.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.CategoryDto;
import ru.persea.productservice.dto.ProductDto;
import ru.persea.productservice.dto.ProductInclude;
import ru.persea.productservice.service.ProductService;

import java.util.List;
import java.util.Set;

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
        @RequestParam("categoryId") @Min(0) Integer categoryId,
        @RequestParam(value = "limit", defaultValue = "5") @Min(0) @Max(50) Integer limit,
        @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page
    ) {
        return ResponseEntity.ok(productService.getProducts(categoryId, limit, page));
    }
    
    @GetMapping("/categories")
    public ResponseEntity<Set<CategoryDto>> getCategories() {
        return ResponseEntity.ok(productService.getCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(
        @PathVariable("id") Long id,
        @RequestParam(name = "include", required = false) Set<ProductInclude> includes
    ) {
        return ResponseEntity.ok(productService.getProduct(id, includes));
    }
}