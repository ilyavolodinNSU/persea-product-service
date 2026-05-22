package ru.persea.productservice.repository.factor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.factor.FactorEnumRuleEntity;

@Repository
public interface FactorEnumRuleRepository extends JpaRepository<FactorEnumRuleEntity, Long> {
    
}
