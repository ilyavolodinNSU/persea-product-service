package ru.persea.productservice.repository.factor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.factor.FactorEnumValueEntity;

@Repository
public interface FactorEnumValueRepository extends JpaRepository<FactorEnumValueEntity, Long> {
    
}
