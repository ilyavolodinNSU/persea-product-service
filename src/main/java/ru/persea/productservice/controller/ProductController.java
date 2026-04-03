package ru.persea.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.ProductDto;
import ru.persea.productservice.dto.ProductInclude;
import ru.persea.productservice.service.ProductService;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(
        @PathVariable("id") Long id,
        @RequestParam(name = "include", required = false) Set<ProductInclude> includes
    ) {
        return ResponseEntity.ok(productService.getProduct(id, includes));
    }
    
}