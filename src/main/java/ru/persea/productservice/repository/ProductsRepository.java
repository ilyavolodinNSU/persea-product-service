package ru.persea.productservice.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.persea.productservice.entity.ProductEntity;

@Repository
public interface ProductsRepository extends JpaRepository<ProductEntity, Long> {
    @Query(value = "select * from autocomplete(:q, :size)", nativeQuery = true)
    public Set<String> findSuggestions(@Param("q") String query, @Param("size") Integer size);

    @Query(value = """
        select * 
        from search(:query, :categoryId, :brandsIds, :minRating, :maxRating, :size, :page)
        """, nativeQuery = true)
    public List<ProductEntity> findProducts(
        @Param("query") String query,
        @Param("categoryId") Integer categoryId,
        @Param("brandsIds") Integer[] brandsIds,
        @Param("minRating") Integer minRating,
        @Param("maxRating") Integer maxRating,
        @Param("size") Integer size,
        @Param("page") Integer page
    );
}
