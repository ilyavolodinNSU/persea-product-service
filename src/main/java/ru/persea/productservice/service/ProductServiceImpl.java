package ru.persea.productservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.persea.productservice.dto.product.brand.request.CreateBrandRequest;
import ru.persea.productservice.dto.product.brand.request.UpdateBrandRequest;
import ru.persea.productservice.dto.product.brand.response.BrandDto;
import ru.persea.productservice.dto.product.category.request.CreateCategoryRequest;
import ru.persea.productservice.dto.product.category.request.UpdateCategoryRequest;
import ru.persea.productservice.dto.product.category.response.CategoryDto;
import ru.persea.productservice.dto.product.product.ProductInclude;
import ru.persea.productservice.dto.product.product.ProductSearchDto;
import ru.persea.productservice.dto.product.product.request.*;
import ru.persea.productservice.dto.product.product.response.ProductResponse;
import ru.persea.productservice.entity.outbox.OutboxEvent;
import ru.persea.productservice.entity.product.BrandEntity;
import ru.persea.productservice.entity.product.CategoryEntity;
import ru.persea.productservice.entity.product.ProductBooleanFactorEntity;
import ru.persea.productservice.entity.product.ProductDocument;
import ru.persea.productservice.entity.product.ProductEntity;
import ru.persea.productservice.entity.product.ProductEnumFactorEntity;
import ru.persea.productservice.entity.product.ProductNumericFactorEntity;
import ru.persea.productservice.mapper.ProductMapper;
import ru.persea.productservice.mapper.ProductSearchMapper;
import ru.persea.productservice.repository.factor.FactorRepository;
import ru.persea.productservice.repository.product.OutboxEventRepository;
import ru.persea.productservice.repository.product.ProductBooleanFactorRepository;
import ru.persea.productservice.repository.product.ProductEnumFactorRepository;
import ru.persea.productservice.repository.product.BrandRepository;
import ru.persea.productservice.repository.product.CategoryRepository;
import ru.persea.productservice.repository.factor.FactorEnumValueRepository;
import ru.persea.productservice.repository.product.ProductNumericFactorRepository;
import ru.persea.productservice.repository.product.ProductsRepository;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
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
    private final OutboxEventRepository outboxEventRepository;
    
    private final ProductMapper productMapper;
    private final ProductSearchMapper productSearchMapper;

    private final ElasticsearchOperations esOperations;
    private final ElasticsearchClient esClient;

    private final RatingService ratingService;

    private final ObjectMapper objectMapper;

    private CategoryEntity getCategoryEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена: " + id));
    }

    private BrandEntity getBrandEntity(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бренд не найден: " + id));
    }

    private ProductEntity getProductEntity(Long id) {
        return productsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Продукт не найден: " + id));
    }

    private void saveFactors(
            ProductEntity product,
            List<ProductNumericFactorRequest> numericFactors,
            List<ProductBooleanFactorRequest> booleanFactors,
            List<ProductEnumFactorRequest> enumFactors
    ) {
        numericFactorRepository.saveAll(
            numericFactors.stream()
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
            booleanFactors.stream()
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
            enumFactors.stream()
                .map(f -> {
                    var factorEnum = new ProductEnumFactorEntity();
                    factorEnum.setProduct(product);
                    factorEnum.setFactor(factorRepository.getReferenceById(f.factorId()));
                    factorEnum.setEnumValue(factorEnumValueRepository.getReferenceById(f.enumValueId()));
                    return factorEnum;
                })
                .toList()
        );
    }

    // categories

    @Override
    @Transactional
    public CategoryDto createCategory(CreateCategoryRequest request) {
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
    public CategoryDto getCategory(Long id) {
        return productMapper.toDto(getCategoryEntity(id));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, UpdateCategoryRequest request) {
        var entity = getCategoryEntity(id);
        entity.setName(request.name());
        entity.setCode(request.code());

        return productMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        getCategoryEntity(id);
        categoryRepository.deleteById(id);
    }

    // brands

    @Override
    public BrandDto createBrand(CreateBrandRequest request) {
        var brand = new BrandEntity();
        brand.setName(request.name());

        return productMapper.toDto(brandRepository.save(brand));
    }

    @Override
    public List<BrandDto> getBrands() {
        return brandRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public BrandDto getBrand(Long id) {
        return productMapper.toDto(getBrandEntity(id));
    }

    @Override
    @Transactional
    public BrandDto updateBrand(Long id, UpdateBrandRequest request) {
        var entity = getBrandEntity(id);
        entity.setName(request.name());

        return productMapper.toDto(brandRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        getBrandEntity(id);
        brandRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        var product = new ProductEntity();
        product.setBrand(getBrandEntity(request.brandId()));
        product.setCategory(getCategoryEntity(request.categoryId()));
        product.setName(request.name());
        product.setImageURI(request.imageURI());

        productsRepository.save(product);

        saveFactors(product, request.numericFactors(), request.booleanFactors(), request.enumFactors());

        productsRepository.flush();
        numericFactorRepository.flush();
        booleanFactorRepository.flush();
        enumFactorRepository.flush();

        product.setRating(ratingService.calculate(product.getId()));

        return getProduct(product.getId(), Set.of(ProductInclude.FACTORS), false);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        var product = getProductEntity(id);
        product.setName(request.name());
        product.setImageURI(request.imageURI());
        product.setBrand(getBrandEntity(request.brandId()));
        product.setCategory(getCategoryEntity(request.categoryId()));

        numericFactorRepository.deleteAllByProductId(id);
        booleanFactorRepository.deleteAllByProductId(id);
        enumFactorRepository.deleteAllByProductId(id);

        saveFactors(product, request.numericFactors(), request.booleanFactors(), request.enumFactors());

        numericFactorRepository.flush();
        booleanFactorRepository.flush();
        enumFactorRepository.flush();

        product.setRating(ratingService.calculate(product.getId()));
        product.setUpdatedAt(Instant.now());

        return getProduct(product.getId(), Set.of(ProductInclude.FACTORS), false);
    }

    @Override
    @Transactional
    public ProductResponse getProduct(Long id, Set<ProductInclude> includes) {
        return getProduct(id, includes, true);
    }

    private ProductResponse getProduct(Long id, Set<ProductInclude> includes, boolean saveOutbox) {
        var product = productsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Продукт не найден: " + id));

        boolean fetchFactors = includes != null && includes.contains(ProductInclude.FACTORS);

        if (fetchFactors && saveOutbox) {
            saveViewOutboxEvent(product.getId());
        }

        return productMapper.toDto(
            product,
            fetchFactors ? numericFactorRepository.findAllWithRules(id) : null,
            fetchFactors ? booleanFactorRepository.findAllWithRules(id) : null,
            fetchFactors ? enumFactorRepository.findAllWithRules(id) : null
        );
    }

    private void saveViewOutboxEvent(Long productId) {
        UUID userId = SecurityUtils.getKeycloakUUID();
        if (userId == null) return;

        try {
            String payloadJson = objectMapper.writeValueAsString(
                UserActionEvent.builder()
                    .userId(userId)
                    .productId(productId)
                    .type("view")
                    .createdAt(Instant.now())
                    .build()
            );

            outboxEventRepository.save(
                OutboxEvent.builder()
                    .aggregateType("product")
                    .aggregateId(productId.toString())
                    .type("ProductViewed")
                    .payload(payloadJson)
                    .timestamp(Instant.now())
                    .build()
            );
        } catch (JacksonException e) {
            log.error("Failed to serialize outbox event for product {}", productId, e);
        }
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        getProductEntity(id);
        productsRepository.deleteById(id);
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
        int effectiveLimit = Math.min(limit, 5);

        SearchRequest searchRequest = SearchRequest.of(s -> s
            .index("products")
            .suggest(sug -> sug
                .suggesters("product-suggest", ps -> ps
                    .prefix(prefix)
                    .completion(c -> c
                        .field("name_suggest")
                        .size(effectiveLimit)
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
