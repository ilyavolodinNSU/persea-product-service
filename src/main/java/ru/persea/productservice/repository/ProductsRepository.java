package ru.persea.productservice.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.ProductEntity;

@Repository
public interface ProductsRepository extends JpaRepository<ProductEntity, Long> {
    public List<ProductEntity> findAllByCategoryId(Integer categoryId, Pageable pageable);
}
