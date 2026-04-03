package ru.persea.productservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.ProductFactorEntity;

@Repository
public interface ProductFactorsRepository extends JpaRepository<ProductFactorEntity, Long> {
    public List<ProductFactorEntity> findByProductId(Long productId);
}
