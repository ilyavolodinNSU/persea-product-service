package ru.persea.productservice.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.dto.product.product.response.ProductBooleanFactorResponse;
import ru.persea.productservice.entity.product.ProductBooleanFactorEntity;

@Repository
public interface ProductBooleanFactorRepository extends JpaRepository<ProductBooleanFactorEntity, Long> {
    public List<ProductBooleanFactorEntity> findAllByProductId(Long productId);

    @Query("""
        select new ru.persea.productservice.dto.product.product.response.ProductBooleanFactorResponse(
            pbf.id, pbf.factor.id, f.name, pbf.value, fbr.impact
        )
        from ProductBooleanFactorEntity pbf
        join pbf.product p
        join pbf.factor f
        join FactorBooleanRuleEntity fbr on fbr.factor.id = pbf.factor.id
            and fbr.category.id = p.category.id
        where p.id = :productId
    """)
    public List<ProductBooleanFactorResponse> findAllWithRules(Long productId);

    @Modifying
    @Query("DELETE FROM ProductBooleanFactorEntity f WHERE f.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);
}
