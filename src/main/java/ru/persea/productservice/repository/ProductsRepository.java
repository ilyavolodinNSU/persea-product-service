package ru.persea.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.ProductEntity;

@Repository
public interface ProductsRepository extends JpaRepository<ProductEntity, Long> {
    
}
