package ru.persea.productservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.product.ProductNumericFactorEntity;

@Repository
public interface ProductNumericFactorRepository extends JpaRepository<ProductNumericFactorEntity, Long> {
    public List<ProductNumericFactorEntity> findAllByProductId(Long productId);
}
