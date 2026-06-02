package ru.persea.productservice.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.product.ProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductsRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByBarcode(String barcode);

    @Query("""
        select p
        from ProductEntity p
        where (:queryEnabled = false or lower(p.name) like :queryPattern)
          and (:categoryId is null or p.category.id = :categoryId)
          and (:brandIdsEmpty = true or p.brand.id in :brandIds)
          and (:minRating is null or p.rating >= :minRating)
          and (:maxRating is null or p.rating <= :maxRating)
        order by p.id asc
        """)
    List<ProductEntity> searchProductsFallback(
        @Param("queryPattern") String queryPattern,
        @Param("queryEnabled") boolean queryEnabled,
        @Param("categoryId") Long categoryId,
        @Param("brandIds") Set<Long> brandIds,
        @Param("brandIdsEmpty") boolean brandIdsEmpty,
        @Param("minRating") Integer minRating,
        @Param("maxRating") Integer maxRating,
        Pageable pageable
    );

    @Query("""
        select distinct p.name
        from ProductEntity p
        where lower(p.name) like :queryPrefix
        order by p.name asc
        """)
    List<String> findNameSuggestionsFallback(
        @Param("queryPrefix") String queryPrefix,
        Pageable pageable
    );
}
