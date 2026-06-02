package ru.persea.productservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggest;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
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
import ru.persea.productservice.entity.product.*;
import ru.persea.productservice.mapper.ProductMapper;
import ru.persea.productservice.mapper.ProductSearchMapper;
import ru.persea.productservice.repository.factor.FactorRepository;
import ru.persea.productservice.repository.factor.FactorEnumValueRepository;
import ru.persea.productservice.repository.product.*;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private FactorRepository factorRepository;
    @Mock
    private ProductsRepository productsRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private ProductNumericFactorRepository numericFactorRepository;
    @Mock
    private ProductBooleanFactorRepository booleanFactorRepository;
    @Mock
    private ProductEnumFactorRepository enumFactorRepository;
    @Mock
    private FactorEnumValueRepository factorEnumValueRepository;
    @Mock
    private OutboxEventRepository outboxEventRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductSearchMapper productSearchMapper;
    @Mock
    private ElasticsearchOperations esOperations;
    @Mock
    private ElasticsearchClient esClient;
    @Mock
    private RatingService ratingService;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    // ================== Category tests ==================
    @Test
    void createCategory_shouldSaveAndReturnDto() {
        CreateCategoryRequest request = new CreateCategoryRequest("Электроника", "electronics");
        CategoryEntity savedEntity = new CategoryEntity(1L, "Электроника", "electronics");
        CategoryDto dto = new CategoryDto(1L, "Электроника", "electronics");

        when(categoryRepository.save(any())).thenReturn(savedEntity);
        when(productMapper.toDto(savedEntity)).thenReturn(dto);

        CategoryDto result = productService.createCategory(request);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getCategories_shouldReturnList() {
        CategoryEntity entity = new CategoryEntity(1L, "Кат", "code");
        CategoryDto dto = new CategoryDto(1L, "Кат", "code");
        when(categoryRepository.findAll()).thenReturn(List.of(entity));
        when(productMapper.toDto(entity)).thenReturn(dto);

        List<CategoryDto> result = productService.getCategories();
        assertThat(result).containsExactly(dto);
    }

    @Test
    void getCategory_existing_shouldReturnDto() {
        CategoryEntity entity = new CategoryEntity(1L, "Кат", "code");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productMapper.toDto(entity)).thenReturn(new CategoryDto(1L, "Кат", "code"));

        CategoryDto result = productService.getCategory(1L);
        assertThat(result.name()).isEqualTo("Кат");
    }

    @Test
    void getCategory_nonExisting_shouldThrow() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> productService.getCategory(99L));
    }

    @Test
    void updateCategory_shouldUpdateAndReturnDto() {
        CategoryEntity existing = new CategoryEntity(1L, "Старое", "old");
        UpdateCategoryRequest request = new UpdateCategoryRequest("Новое", "new");
        CategoryEntity saved = new CategoryEntity(1L, "Новое", "new");
        CategoryDto dto = new CategoryDto(1L, "Новое", "new");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(saved);
        when(productMapper.toDto(saved)).thenReturn(dto);

        CategoryDto result = productService.updateCategory(1L, request);
        assertThat(result).isEqualTo(dto);
        assertThat(existing.getName()).isEqualTo("Новое");
    }

    @Test
    void deleteCategory_shouldDeleteIfExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new CategoryEntity()));
        productService.deleteCategory(1L);
        verify(categoryRepository).deleteById(1L);
    }

    // ================== Brand tests ==================
    @Test
    void createBrand_shouldSaveAndReturnDto() {
        CreateBrandRequest request = new CreateBrandRequest("Samsung", "desc");
        BrandEntity savedEntity = new BrandEntity(1L, "Samsung");
        BrandDto dto = new BrandDto(1L, "Samsung");

        when(brandRepository.save(any())).thenReturn(savedEntity);
        when(productMapper.toDto(savedEntity)).thenReturn(dto);

        BrandDto result = productService.createBrand(request);
        assertThat(result).isEqualTo(dto);
    }

    // ... остальные brand тесты пропущены для краткости (аналогичны категориям)

    // ================== Product tests ==================
    @Test
    void createProduct_shouldSaveProductAndFactorsAndReturnDto() {
        CreateProductRequest request = new CreateProductRequest(
                "Товар", 1L, 2L, "img", "4600000000011",
                List.of(new ProductNumericFactorRequest(10L, 5.0)),
                List.of(new ProductBooleanFactorRequest(20L, true)),
                List.of(new ProductEnumFactorRequest(30L, 40L))
        );
        BrandEntity brand = new BrandEntity(2L, "Бренд");
        CategoryEntity category = new CategoryEntity(1L, "Кат", "код");
        ProductResponse responseDto = mock(ProductResponse.class);

        when(brandRepository.findById(2L)).thenReturn(Optional.of(brand));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // save должен установить ID переданному продукту
        when(productsRepository.save(any(ProductEntity.class))).thenAnswer(invocation -> {
            ProductEntity p = invocation.getArgument(0);
            p.setId(100L);
            return p;
        });

        when(factorRepository.getReferenceById(10L)).thenReturn(mock());
        when(factorRepository.getReferenceById(20L)).thenReturn(mock());
        when(factorRepository.getReferenceById(30L)).thenReturn(mock());
        when(factorEnumValueRepository.getReferenceById(40L)).thenReturn(mock());
        when(ratingService.calculate(100L)).thenReturn(5);

        // getProduct внутри createProduct
        ProductEntity createdProduct = new ProductEntity();
        createdProduct.setId(100L);
        createdProduct.setName("Товар");
        createdProduct.setCategory(category);
        createdProduct.setBrand(brand);
        when(productsRepository.findById(100L)).thenReturn(Optional.of(createdProduct));
        when(numericFactorRepository.findAllWithRules(100L)).thenReturn(List.of());
        when(booleanFactorRepository.findAllWithRules(100L)).thenReturn(List.of());
        when(enumFactorRepository.findAllWithRules(100L)).thenReturn(List.of());
        when(productMapper.toDto(eq(createdProduct), any(), any(), any())).thenReturn(responseDto);

        ProductResponse result = productService.createProduct(request);

        assertThat(result).isEqualTo(responseDto);
        verify(productsRepository).save(argThat(product -> "4600000000011".equals(product.getBarcode())));
        verify(productsRepository).save(any(ProductEntity.class));
        verify(numericFactorRepository).saveAll(any());
        verify(booleanFactorRepository).saveAll(any());
        verify(enumFactorRepository).saveAll(any());
        verify(ratingService).calculate(100L);
    }

    @Test
    void getProduct_withFactors_shouldReturnDtoWithFactors() {
        Set<ProductInclude> includes = Set.of(ProductInclude.FACTORS);
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        ProductResponse dto = mock(ProductResponse.class);

        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));
        when(numericFactorRepository.findAllWithRules(1L)).thenReturn(List.of());
        when(booleanFactorRepository.findAllWithRules(1L)).thenReturn(List.of());
        when(enumFactorRepository.findAllWithRules(1L)).thenReturn(List.of());
        when(productMapper.toDto(eq(product), any(), any(), any())).thenReturn(dto);

        ProductResponse result = productService.getProduct(1L, includes);

        assertThat(result).isEqualTo(dto);
        verify(outboxEventRepository, never()).save(any());
    }

    @Test
    void getProduct_withoutFactors_shouldReturnDtoWithoutFactorLists() {
        Set<ProductInclude> includes = Collections.emptySet();
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        ProductResponse dto = mock(ProductResponse.class);

        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(eq(product), isNull(), isNull(), isNull())).thenReturn(dto);

        ProductResponse result = productService.getProduct(1L, includes);

        assertThat(result).isEqualTo(dto);
        verify(numericFactorRepository, never()).findAllWithRules(anyLong());
    }

    @Test
    void getProductByBarcode_shouldReturnProductAndSaveScanOutbox() throws Exception {
        UUID userId = UUID.randomUUID();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken(
                        org.springframework.security.oauth2.jwt.Jwt.withTokenValue("token")
                                .header("alg", "none")
                                .claim("sub", userId.toString())
                                .build()
                )
        );

        ProductEntity product = new ProductEntity();
        product.setId(7L);
        product.setName("Вода");
        product.setBarcode("4600000000011");
        ProductResponse dto = mock(ProductResponse.class);

        when(productsRepository.findByBarcode("4600000000011")).thenReturn(Optional.of(product));
        when(productsRepository.findById(7L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(eq(product), isNull(), isNull(), isNull())).thenReturn(dto);
        when(objectMapper.writeValueAsString(any(UserActionEvent.class))).thenReturn("{\"type\":\"scan\"}");

        ProductResponse result = productService.getProductByBarcode(" 4600000000011 ", Collections.emptySet());

        assertThat(result).isEqualTo(dto);
        verify(outboxEventRepository).save(argThat(event ->
                "product".equals(event.getAggregateType())
                        && "7".equals(event.getAggregateId())
                        && "ProductViewed".equals(event.getType())
                        && "{\"type\":\"scan\"}".equals(event.getPayload())
        ));
        verify(objectMapper).writeValueAsString(argThat(argument -> {
            if (!(argument instanceof UserActionEvent event)) return false;
            return userId.equals(event.userId())
                    && Long.valueOf(7L).equals(event.productId())
                    && "scan".equals(event.type())
                    && event.createdAt() != null;
        }));
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }

    @Test
    void updateProduct_shouldUpdateAndReturnDto() {
        UpdateProductRequest request = new UpdateProductRequest(
                "Новое имя", 3L, 4L, "newImg", "4600000000028",
                List.of(new ProductNumericFactorRequest(11L, 6.0)),
                List.of(),
                List.of()
        );
        ProductEntity existing = new ProductEntity();
        existing.setId(10L);
        existing.setName("Старое");
        BrandEntity brand = new BrandEntity(4L, "Бренд");
        CategoryEntity category = new CategoryEntity(3L, "Кат", "к");
        ProductResponse responseDto = mock(ProductResponse.class);

        when(productsRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(brandRepository.findById(4L)).thenReturn(Optional.of(brand));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        doNothing().when(numericFactorRepository).deleteAllByProductId(10L);
        doNothing().when(booleanFactorRepository).deleteAllByProductId(10L);
        doNothing().when(enumFactorRepository).deleteAllByProductId(10L);
        when(factorRepository.getReferenceById(11L)).thenReturn(mock());
        when(ratingService.calculate(10L)).thenReturn(4);
        when(productsRepository.findById(10L)).thenReturn(Optional.of(existing)); // для getProduct
        when(numericFactorRepository.findAllWithRules(10L)).thenReturn(List.of());
        when(booleanFactorRepository.findAllWithRules(10L)).thenReturn(List.of());
        when(enumFactorRepository.findAllWithRules(10L)).thenReturn(List.of());
        when(productMapper.toDto(eq(existing), any(), any(), any())).thenReturn(responseDto);

        ProductResponse result = productService.updateProduct(10L, request);

        assertThat(result).isEqualTo(responseDto);
        assertThat(existing.getName()).isEqualTo("Новое имя");
        assertThat(existing.getBarcode()).isEqualTo("4600000000028");
        assertThat(existing.getUpdatedAt()).isNotNull();
        verify(numericFactorRepository).deleteAllByProductId(10L);
        verify(booleanFactorRepository).deleteAllByProductId(10L);
        verify(enumFactorRepository).deleteAllByProductId(10L);
    }

    @Test
    void deleteProduct_shouldDeleteIfExists() {
        when(productsRepository.findById(1L)).thenReturn(Optional.of(new ProductEntity()));
        productService.deleteProduct(1L);
        verify(productsRepository).deleteById(1L);
    }

    // ================== Search tests ==================
    @Test
    void searchProducts_shouldReturnDtos() {
        Pageable pageable = Pageable.unpaged();
        ProductDocument doc1 = new ProductDocument(1L, "Телефон", 1, 2, 5, "img1", Instant.now());
        SearchHit<ProductDocument> hit = mock(SearchHit.class);
        when(hit.getContent()).thenReturn(doc1);
        SearchHits<ProductDocument> searchHits = mock(SearchHits.class);
        when(searchHits.stream()).thenReturn(List.of(hit).stream());
        when(esOperations.search(any(NativeQuery.class), eq(ProductDocument.class))).thenReturn(searchHits);
        ProductSearchDto dto1 = new ProductSearchDto(1L, "Телефон", 5, "img1");
        when(productSearchMapper.toDto(doc1)).thenReturn(dto1);

        List<ProductSearchDto> result = productService.searchProducts("тел", 1, Set.of(2), 3, 5, pageable);

        assertThat(result).containsExactly(dto1);
        verify(esOperations).search(any(NativeQuery.class), eq(ProductDocument.class));
    }

    @Test
    void searchProducts_whenElasticFails_shouldFallbackToDatabase() {
        Pageable pageable = Pageable.unpaged();
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setName("Вода");
        product.setRating(82);
        product.setImageURI("img");

        when(esOperations.search(any(NativeQuery.class), eq(ProductDocument.class)))
                .thenThrow(new RuntimeException("index is not ready"));
        when(productsRepository.searchProductsFallback(
                eq("%вода%"),
                eq(true),
                eq(1L),
                eq(Set.of(2L)),
                eq(false),
                eq(70),
                isNull(),
                eq(pageable)
        )).thenReturn(List.of(product));

        List<ProductSearchDto> result = productService.searchProducts(" вода ", 1, Set.of(2), 70, null, pageable);

        assertThat(result).containsExactly(new ProductSearchDto(1L, "Вода", 82, "img"));
    }

    @Test
    void searchProducts_whenElasticReturnsEmpty_shouldFallbackToDatabase() {
        Pageable pageable = Pageable.unpaged();
        ProductEntity product = new ProductEntity();
        product.setId(2L);
        product.setName("Акваника");
        product.setRating(90);
        product.setImageURI("img2");

        SearchHits<ProductDocument> searchHits = mock(SearchHits.class);
        when(searchHits.stream()).thenReturn(List.<SearchHit<ProductDocument>>of().stream());
        when(esOperations.search(any(NativeQuery.class), eq(ProductDocument.class))).thenReturn(searchHits);
        when(productsRepository.searchProductsFallback(
                eq("%акваника%"),
                eq(true),
                isNull(),
                eq(Set.of(-1L)),
                eq(true),
                isNull(),
                isNull(),
                eq(pageable)
        )).thenReturn(List.of(product));

        List<ProductSearchDto> result = productService.searchProducts("Акваника", null, null, null, null, pageable);

        assertThat(result).containsExactly(new ProductSearchDto(2L, "Акваника", 90, "img2"));
    }

    // ================== Suggestions tests ==================
    @Test
    void getSuggestions_shouldReturnSuggestionsFromElastic() throws Exception {
        when(productsRepository.findNameSuggestionsFallback(eq("тел%"), any()))
                .thenReturn(List.of());
        SearchResponse<Void> searchResponse = mock(SearchResponse.class);
        Map<String, List<Suggestion<Void>>> map = new HashMap<>();
        List<Suggestion<Void>> suggestions = new ArrayList<>();
        Suggestion<Void> suggestion = mock(Suggestion.class);
        CompletionSuggest<Void> completion = mock(CompletionSuggest.class);
        List<CompletionSuggestOption<Void>> options = List.of(
                CompletionSuggestOption.of(o -> o.text("телефон")),
                CompletionSuggestOption.of(o -> o.text("телевизор"))
        );
        when(completion.options()).thenReturn(options);
        when(suggestion.completion()).thenReturn(completion);
        suggestions.add(suggestion);
        map.put("product-suggest", suggestions);
        when(searchResponse.suggest()).thenReturn(map);
        when(esClient.search(any(SearchRequest.class), eq(Void.class))).thenReturn(searchResponse);

        List<String> result = productService.getSuggestions("тел", 3);

        assertThat(result).containsExactly("телефон", "телевизор");
    }

    @Test
    void getSuggestions_whenDatabaseReturnsValues_shouldUseDatabase() throws Exception {
        when(productsRepository.findNameSuggestionsFallback(eq("аква%"), any()))
                .thenReturn(List.of("Акваника"));

        List<String> result = productService.getSuggestions(" Аква ", 3);

        assertThat(result).containsExactly("Акваника");
        verify(esClient, never()).search(any(SearchRequest.class), eq(Void.class));
    }
}
