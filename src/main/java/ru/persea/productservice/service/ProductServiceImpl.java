package ru.persea.productservice.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.SuggestionBuilders;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.CategoryDto;
import ru.persea.productservice.dto.FactorDto;
import ru.persea.productservice.dto.ProductDto;
import ru.persea.productservice.dto.ProductInclude;
import ru.persea.productservice.dto.ProductSearchDto;
import ru.persea.productservice.entity.ProductDocument;
import ru.persea.productservice.entity.ProductEntity;
import ru.persea.productservice.entity.ProductFactorEntity;
import ru.persea.productservice.mapper.CategoryMapper;
import ru.persea.productservice.mapper.ProductFactorMapper;
import ru.persea.productservice.mapper.ProductMapper;
import ru.persea.productservice.mapper.ProductSearchMapper;
import ru.persea.productservice.repository.CategoriesRepository;
import ru.persea.productservice.repository.ProductFactorsRepository;
import ru.persea.productservice.repository.ProductsRepository;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository productsRepository;
    private final ProductFactorsRepository productFactorsRepository;
    private final CategoriesRepository categoriesRepository;
    
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final ProductSearchMapper productSearchMapper;

    private final ElasticsearchOperations esOperations;
    private final ElasticsearchClient esClient;

    @Override
    public ProductDto getProduct(Long id, Set<ProductInclude> includes) {
        ProductEntity entity = productsRepository.findById(id).orElseThrow();
        List<ProductFactorEntity> factors = null;

        if (includes != null && includes.contains(ProductInclude.FACTORS))
            factors = productFactorsRepository.findByProductId(id);

        return productMapper.toDto(entity, factors);
    }

    @Override
    public Set<CategoryDto> getCategories() {
        return categoriesRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public List<ProductSearchDto> searchProducts(
        String query,
        Integer categoryId, 
        Set<Integer> brandIds, 
        Integer minRating, 
        Integer maxRating, 
        Pageable pageable
    ) {
        System.out.println("Sort: " + pageable.getSort()); 

        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

        if (query != null && !query.isBlank()) {
            boolBuilder.must(Query.of(q -> q
                .match(m -> m
                    .field("name")
                    .query(query)
                    .fuzziness("2")
                )
            ));
        } else boolBuilder.must(Query.of(q -> q.matchAll(m -> m)));

        if (categoryId != null) {
            boolBuilder.filter(Query.of(q -> q
                .term(t -> t.field("category_id").value(categoryId))
            ));
        }

        if (brandIds != null && !brandIds.isEmpty()) {
            boolBuilder.filter(Query.of(q -> q
                .terms(t -> t.field("brand_id").terms(tv -> tv.value(
                    brandIds.stream()
                        .map(id -> FieldValue.of(fv -> fv.longValue(id.longValue())))
                        .toList()
                )))
            ));
        }

        if (minRating != null || maxRating != null) {
            boolBuilder.filter(Query.of(q -> q
                .range(r -> r
                    .number(n -> {
                        n.field("rating");
                        if (minRating != null) n.gte(minRating.doubleValue());
                        if (maxRating != null) n.lte(maxRating.doubleValue()); 
                        return n;
                    })
                )
            ));
        }

        NativeQuery searchQuery = NativeQuery.builder()
            .withQuery(Query.of(q -> q.bool(boolBuilder.build())))
            .withPageable(pageable)
            .build();

        return esOperations.search(searchQuery, ProductDocument.class).stream()
                .map(SearchHit::getContent)
                .map(productSearchMapper::toDto)
                .toList();
    }

    @Override
    public Set<String> getSuggestions(String prefix, int limit) {
        SearchRequest searchRequest = SearchRequest.of(s -> s
            .index("products")
            .suggest(sug -> sug
                .suggesters("product-suggest", ps -> ps
                    .prefix(prefix)
                    .completion(c -> c
                        .field("name_suggest")
                        .size(limit)
                        .skipDuplicates(true)
                        .fuzzy(f -> f
                            .fuzziness("AUTO")
                            .minLength(3)
                        )
                    )
                )
            )
        );

        try {
            SearchResponse<Void> searchResponse = esClient.search(searchRequest, Void.class);
            
            if (searchResponse.suggest() != null && searchResponse.suggest().containsKey("product-suggest")) {                
                return searchResponse.suggest().get("product-suggest").stream()
                    .flatMap(s -> s.completion().options().stream())
                    .map(CompletionSuggestOption::text)
                    .collect(Collectors.toSet());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashSet<>();
    }
}
