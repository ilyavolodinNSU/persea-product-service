package ru.persea.productservice.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.dto.product.product.response.ProductNumericFactorResponse;
import ru.persea.productservice.entity.product.ProductNumericFactorEntity;

@Repository
public interface ProductNumericFactorRepository extends JpaRepository<ProductNumericFactorEntity, Long> {
    public List<ProductNumericFactorEntity> findAllByProductId(Long productId);

    @Query("""
        select new ru.persea.productservice.dto.product.product.response.ProductNumericFactorResponse(
            pnf.id, pnf.factor.id, f.name, u.name, pnf.amount, fnr.minValue, fnr.maxValue
        )
        from ProductNumericFactorEntity pnf
        join pnf.product p
        join pnf.factor f
        join FactorNumericRuleEntity fnr on fnr.factor.id = pnf.factor.id
            and fnr.category.id = p.category.id
        join fnr.unit u
        where p.id = :productId
    """)
    public List<ProductNumericFactorResponse> findAllWithRules(Long productId);

    @Modifying
    @Query("DELETE FROM ProductNumericFactorEntity f WHERE f.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);
}
