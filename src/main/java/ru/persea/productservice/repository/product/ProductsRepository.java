package ru.persea.productservice.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.product.ProductEntity;

@Repository
public interface ProductsRepository extends JpaRepository<ProductEntity, Long> {

}
