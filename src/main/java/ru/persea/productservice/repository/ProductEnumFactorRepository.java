package ru.persea.productservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.product.ProductEnumFactorEntity;
import ru.persea.productservice.entity.product.ProductNumericFactorEntity;

@Repository
public interface ProductEnumFactorRepository extends JpaRepository<ProductEnumFactorEntity, Long> {
    public List<ProductEnumFactorEntity> findAllByProductId(Long productId);
}
