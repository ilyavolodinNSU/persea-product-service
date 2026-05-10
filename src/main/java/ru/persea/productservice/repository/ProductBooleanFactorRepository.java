package ru.persea.productservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.product.ProductBooleanFactorEntity;
import ru.persea.productservice.entity.product.ProductEnumFactorEntity;

@Repository
public interface ProductBooleanFactorRepository extends JpaRepository<ProductBooleanFactorEntity, Long> {
    public List<ProductBooleanFactorEntity> findAllByProductId(Long productId);
}
