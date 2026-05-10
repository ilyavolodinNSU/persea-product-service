package ru.persea.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.product.BrandEntity;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
    
}
