package ru.persea.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.product.product.ProductSearchDto;
import ru.persea.productservice.service.ProductService;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductSearchController {
    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasRole('APP_USER')")
    public ResponseEntity<List<ProductSearchDto>> searchProducts(
        @RequestParam(value = "q", required = false) String query,
        @RequestParam(value = "q_raw", required = false) String rawQuery,
        @RequestParam(value = "query_raw", required = false) String rawQueryAlias,
        @RequestParam(value = "q_base64", required = false) String queryBase64,
        @RequestParam(value = "query_base64", required = false) String queryBase64Alias,
        @RequestParam(value = "category_id", required = false) Integer categoryId,
        @RequestParam(value = "brand_ids", required = false) Set<Integer> brandsIds,
        @RequestParam(value = "min_rating", required = false) Integer minRating,
        @RequestParam(value = "max_rating", required = false) Integer maxRating,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            productService.searchProducts(
                resolveQuery(query, queryBase64, queryBase64Alias, rawQuery, rawQueryAlias),
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
        @RequestParam(value = "q", required = false) String query,
        @RequestParam(value = "q_raw", required = false) String rawQuery,
        @RequestParam(value = "query_raw", required = false) String rawQueryAlias,
        @RequestParam(value = "q_base64", required = false) String queryBase64,
        @RequestParam(value = "query_base64", required = false) String queryBase64Alias,
        @RequestParam(value = "limit", defaultValue = "5", required = false) Integer limit
    ) {
        return ResponseEntity.ok(productService.getSuggestions(
            resolveQuery(query, queryBase64, queryBase64Alias, rawQuery, rawQueryAlias),
            limit
        ));
    }

    private String resolveQuery(
        String query,
        String queryBase64,
        String queryBase64Alias,
        String rawQuery,
        String rawQueryAlias
    ) {
        String plainQuery = firstNonBlank(rawQuery, rawQueryAlias);
        if (plainQuery != null) return plainQuery;

        String encodedQuery = firstNonBlank(query, queryBase64, queryBase64Alias);
        if (encodedQuery == null) return null;

        try {
            byte[] decodedBytes = decodeBase64(encodedQuery);
            return StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(decodedBytes))
                .toString();
        } catch (IllegalArgumentException | CharacterCodingException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "q must contain a valid UTF-8 Base64 encoded query",
                e
            );
        }
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) return value;
        }
        return null;
    }

    private byte[] decodeBase64(String value) {
        String normalizedValue = addBase64Padding(value.trim());
        try {
            return Base64.getDecoder().decode(normalizedValue);
        } catch (IllegalArgumentException ignored) {
            return Base64.getUrlDecoder().decode(normalizedValue);
        }
    }

    private String addBase64Padding(String value) {
        int paddingLength = (4 - value.length() % 4) % 4;
        return value + "=".repeat(paddingLength);
    }
}
