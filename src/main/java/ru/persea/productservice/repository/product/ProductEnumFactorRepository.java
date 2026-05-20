package ru.persea.productservice.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.dto.product.product.response.ProductEnumFactorResponse;
import ru.persea.productservice.entity.product.ProductEnumFactorEntity;

@Repository
public interface ProductEnumFactorRepository extends JpaRepository<ProductEnumFactorEntity, Long> {
    public List<ProductEnumFactorEntity> findAllByProductId(Long productId);

    @Query("""
        select new ru.persea.productservice.dto.product.response.ProductEnumFactorResponse(
            pef.id, f.id, f.name, ev.value, fer.impact
        )
        from ProductEnumFactorEntity pef
        join pef.product p
        join pef.enumValue ev
        join ev.factor f
        join FactorEnumRuleEntity fer on fer.enumValue.id = ev.id
            and fer.category.id = p.category.id
        where p.id = :productId
    """)
    public List<ProductEnumFactorResponse> findAllWithRules(Long productId);

    @Modifying
    @Query("DELETE FROM ProductEnumFactorEntity f WHERE f.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);
}
