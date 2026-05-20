package ru.persea.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.factor.FactorBooleanRuleEntity;

@Repository
public interface FactorBooleanRuleRepository extends JpaRepository<FactorBooleanRuleEntity, Long> {
    
}
