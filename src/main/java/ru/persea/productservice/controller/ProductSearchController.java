package ru.persea.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.product.product.ProductSearchDto;
import ru.persea.productservice.service.ProductService;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductSearchController {
    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasRole('APP_USER')")
    public ResponseEntity<List<ProductSearchDto>> searchProducts(
        @RequestParam(value = "q", required = false) String query,
        @RequestParam(value = "category_id", required = false) Integer categoryId,
        @RequestParam(value = "brand_ids", required = false) Set<Integer> brandsIds,
        @RequestParam(value = "min_rating", required = false) Integer minRating,
        @RequestParam(value = "max_rating", required = false) Integer maxRating,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            productService.searchProducts(
                query,
                categoryId, 
                brandsIds, 
                minRating, 
                maxRating, 
                pageable
            )
        );
    }

    @GetMapping("/suggestions")
    @PreAuthorize("hasRole('APP_USER')")
    public ResponseEntity<List<String>> getSuggestions(
        @RequestParam("q") String query, 
        @RequestParam(value = "limit", defaultValue = "5", required = false) Integer limit
    ) {
        return ResponseEntity.ok(productService.getSuggestions(query, limit));
    }
}