package ru.persea.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.CategoryEntity;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoryEntity, Long> {
    
}
