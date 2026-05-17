package ru.persea.productservice.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.dto.product.response.ProductNumericFactorResponse;
import ru.persea.productservice.entity.product.CategoryEntity;
import ru.persea.productservice.entity.product.ProductEntity;

@Repository
public interface ProductsRepository extends JpaRepository<ProductEntity, Long> {

}
