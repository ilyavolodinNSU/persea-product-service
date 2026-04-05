package ru.persea.productservice.service;

import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import ru.persea.productservice.entity.ProductEntity;

public class ProductSpecification {
    public static Specification<ProductEntity> hasCategoryId(Integer categoryId) {
        return (root, cq, cb) -> {
            return categoryId == null 
                ? cb.conjunction()
                : cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<ProductEntity> hasBrandIds(Set<Integer> brandsIds) {
        return (root, cq, cb) -> {
            return brandsIds == null || brandsIds.isEmpty()
                ? cb.conjunction()
                : root.get("brand").get("id").in(brandsIds);
        };
    }

    public static Specification<ProductEntity> ratingBetween(Integer minRating, Integer maxRating) {
        return (root, cq, cb) -> {
            return minRating == null ||  maxRating == null
                ? cb.conjunction()
                : cb.between(root.get("rating"), minRating, maxRating);
        };
    }
}
