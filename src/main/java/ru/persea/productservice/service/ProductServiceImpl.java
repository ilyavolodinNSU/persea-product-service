package ru.persea.productservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import lombok.RequiredArgsConstructor;
import ru.persea.productservice.dto.product.BrandDto;
import ru.persea.productservice.dto.product.CategoryDto;
import ru.persea.productservice.dto.product.ProductInclude;
import ru.persea.productservice.dto.product.ProductSearchDto;
import ru.persea.productservice.dto.product.request.CreateCategory;
import ru.persea.productservice.dto.product.request.CreateProductRequest;
import ru.persea.productservice.dto.product.response.ProductResponse;
import ru.persea.productservice.entity.product.BrandEntity;
import ru.persea.productservice.entity.product.CategoryEntity;
import ru.persea.productservice.entity.product.ProductBooleanFactorEntity;
import ru.persea.productservice.entity.product.ProductDocument;
import ru.persea.productservice.entity.product.ProductEntity;
import ru.persea.productservice.entity.product.ProductEnumFactorEntity;
import ru.persea.productservice.entity.product.ProductNumericFactorEntity;
import ru.persea.productservice.mapper.ProductMapper;
import ru.persea.productservice.mapper.ProductSearchMapper;
import ru.persea.productservice.repository.FactorRepository;
import ru.persea.productservice.repository.ProductBooleanFactorRepository;
import ru.persea.productservice.repository.ProductEnumFactorRepository;
import ru.persea.productservice.repository.BrandRepository;
import ru.persea.productservice.repository.CategoryRepository;
import ru.persea.productservice.repository.FactorEnumValueRepository;
import ru.persea.productservice.repository.ProductNumericFactorRepository;
import ru.persea.productservice.repository.ProductsRepository;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final FactorRepository factorRepository;
    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductNumericFactorRepository numericFactorRepository;
    private final ProductBooleanFactorRepository booleanFactorRepository;
    private final ProductEnumFactorRepository enumFactorRepository;
    private final FactorEnumValueRepository factorEnumValueRepository;
    
    private final ProductMapper productMapper;
    private final ProductSearchMapper productSearchMapper;

    private final ElasticsearchOperations esOperations;
    private final ElasticsearchClient esClient;

    private final RatingService ratingService;

    //private final KafkaTemplate<String, UserActionEvent> kafkaTemplate;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        var product = new ProductEntity();
        product.setBrand(brandRepository.getReferenceById(request.brandId()));
        product.setCategory(categoryRepository.getReferenceById(request.categoryId()));
        product.setName(request.name());
        product.setImageURI(request.imageURI());

        productsRepository.save(product);

        numericFactorRepository.saveAll(
            request.numericFactors().stream()
                .map(f -> {
                    var factorNum = new ProductNumericFactorEntity();
                    factorNum.setProduct(product);
                    factorNum.setFactor(factorRepository.getReferenceById(f.factorId()));
                    factorNum.setAmount(f.amount());
                    return factorNum;
                })
                .toList()
        );

        booleanFactorRepository.saveAll(
            request.booleanFactors().stream()
                .map(f -> {
                    var factorBool = new ProductBooleanFactorEntity();
                    factorBool.setProduct(product);
                    factorBool.setFactor(factorRepository.getReferenceById(f.factorId()));
                    factorBool.setValue(f.value());
                    return factorBool;
                })
                .toList()
        );

        enumFactorRepository.saveAll(
            request.enumFactors().stream()
                .map(f -> {
                    var factorEnum = new ProductEnumFactorEntity();
                    factorEnum.setProduct(product);
                    factorEnum.setFactor(factorRepository.getReferenceById(f.factorId()));
                    factorEnum.setEnumValue(factorEnumValueRepository.getReferenceById(f.enumValueId()));
                    return factorEnum;
                })
                .toList()
        );

        product.setRating(ratingService.calculate(product.getId()));
        productsRepository.save(product);

        return getProduct(product.getId(), Set.of(ProductInclude.FACTORS));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id, Set<ProductInclude> includes) {
        var product = productsRepository.findById(id).orElseThrow();

        boolean fetchFactors = includes != null && includes.contains(ProductInclude.FACTORS);

        // kafkaTemplate.send("user-actions", new UserActionEvent(userId, "view"));

        System.out.println(numericFactorRepository.findAllWithRules(id));

        return productMapper.toDto(
            product,
            fetchFactors ? numericFactorRepository.findAllWithRules(id) : null,
            fetchFactors ? booleanFactorRepository.findAllWithRules(id) : null,
            fetchFactors ? enumFactorRepository.findAllWithRules(id) : null
        );
    }

    @Override
    public CategoryDto createCategory(CreateCategory request) {
        var entity = new CategoryEntity();
        entity.setName(request.name());
        entity.setCode(request.code());

        return productMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public BrandDto createBrand(String name) {
        var brand = new BrandEntity();
        brand.setName(name);

        return productMapper.toDto(brandRepository.save(brand));
    }

    @Override
    public List<BrandDto> getBrands() {
        return brandRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
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
    public List<String> getSuggestions(String prefix, int limit) {
        if (prefix==null || prefix.isBlank() || limit > 5) return new ArrayList<>();

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
                    .toList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
