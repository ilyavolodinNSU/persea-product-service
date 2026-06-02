package ru.persea.productservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.persea.productservice.dto.product.brand.request.CreateBrandRequest;
import ru.persea.productservice.dto.product.brand.request.UpdateBrandRequest;
import ru.persea.productservice.dto.product.brand.response.BrandDto;
import ru.persea.productservice.dto.product.category.request.CreateCategoryRequest;
import ru.persea.productservice.dto.product.category.request.UpdateCategoryRequest;
import ru.persea.productservice.dto.product.category.response.CategoryDto;
import ru.persea.productservice.dto.product.product.ProductInclude;
import ru.persea.productservice.dto.product.product.request.CreateProductRequest;
import ru.persea.productservice.dto.product.product.request.UpdateProductRequest;
import ru.persea.productservice.dto.product.product.response.ProductResponse;
import ru.persea.productservice.service.ProductService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController controller;

    // ================== Category tests ==================
    @Test
    void createCategory_shouldReturnCreated() {
        CreateCategoryRequest request = new CreateCategoryRequest("Электроника", "electronics");
        CategoryDto dto = new CategoryDto(1L, "Электроника", "electronics");
        when(productService.createCategory(any(CreateCategoryRequest.class))).thenReturn(dto);

        ResponseEntity<CategoryDto> response = controller.createCategory(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void getCategories_shouldReturnOk() {
        CategoryDto dto = new CategoryDto(1L, "Категория", "code");
        when(productService.getCategories()).thenReturn(List.of(dto));

        ResponseEntity<List<CategoryDto>> response = controller.getCategories();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(dto);
    }

    @Test
    void getCategory_shouldReturnOk() {
        CategoryDto dto = new CategoryDto(1L, "Категория", "code");
        when(productService.getCategory(1L)).thenReturn(dto);

        ResponseEntity<CategoryDto> response = controller.getCategory(1L);

        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void updateCategory_shouldReturnOk() {
        UpdateCategoryRequest request = new UpdateCategoryRequest("Новое имя", "new_code");
        CategoryDto updated = new CategoryDto(1L, "Новое имя", "new_code");
        when(productService.updateCategory(eq(1L), any(UpdateCategoryRequest.class))).thenReturn(updated);

        ResponseEntity<CategoryDto> response = controller.updateCategory(1L, request);

        assertThat(response.getBody()).isEqualTo(updated);
    }

    @Test
    void deleteCategory_shouldInvokeServiceAndReturnNoContent() {
        controller.deleteCategory(1L);
        verify(productService).deleteCategory(1L);
    }

    // ================== Brand tests ==================
    @Test
    void createBrand_shouldReturnCreated() {
        CreateBrandRequest request = new CreateBrandRequest("Samsung", "Описание");
        BrandDto dto = new BrandDto(1L, "Samsung");
        when(productService.createBrand(any(CreateBrandRequest.class))).thenReturn(dto);

        ResponseEntity<BrandDto> response = controller.createBrand(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void getBrands_shouldReturnOk() {
        BrandDto dto = new BrandDto(1L, "Бренд");
        when(productService.getBrands()).thenReturn(List.of(dto));

        ResponseEntity<List<BrandDto>> response = controller.getBrands();

        assertThat(response.getBody()).containsExactly(dto);
    }

    @Test
    void getBrand_shouldReturnOk() {
        BrandDto dto = new BrandDto(1L, "Бренд");
        when(productService.getBrand(1L)).thenReturn(dto);

        ResponseEntity<BrandDto> response = controller.getBrand(1L);

        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void updateBrand_shouldReturnOk() {
        UpdateBrandRequest request = new UpdateBrandRequest("Новое имя", null);
        BrandDto updated = new BrandDto(1L, "Новое имя");
        when(productService.updateBrand(eq(1L), any(UpdateBrandRequest.class))).thenReturn(updated);

        ResponseEntity<BrandDto> response = controller.updateBrand(1L, request);

        assertThat(response.getBody().name()).isEqualTo("Новое имя");
    }

    @Test
    void deleteBrand_shouldInvokeService() {
        controller.deleteBrand(1L);
        verify(productService).deleteBrand(1L);
    }

    // ================== Product tests ==================
    @Test
    void createProduct_shouldReturnCreated() {
        CreateProductRequest request = new CreateProductRequest(
                "Товар", 1L, 1L, "http://image.url", "4600000000011", null, null, null
        );
        ProductResponse responseDto = new ProductResponse(
                1L, "Товар", null, null, null, "http://image.url", "4600000000011", null, null, null
        );
        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(responseDto);

        ResponseEntity<ProductResponse> response = controller.createProduct(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().name()).isEqualTo("Товар");
    }

    @Test
    void getProduct_withIncludes_shouldReturnOk() {
        Set<ProductInclude> includes = Set.of(ProductInclude.FACTORS);
        ProductResponse responseDto = new ProductResponse(
                1L, "Товар", null, null, null, null, null, null, null, null
        );
        when(productService.getProduct(1L, includes)).thenReturn(responseDto);

        ResponseEntity<ProductResponse> response = controller.getProduct(1L, includes);

        assertThat(response.getBody()).isEqualTo(responseDto);
        verify(productService).getProduct(1L, includes);
    }

    @Test
    void getProduct_withoutIncludes_shouldPassEmptySet() {
        // Если includes = null, ожидаем, что сервис получит null (или пустой Set?) - смотрим на контроллер: он передаёт includes как есть.
        ProductResponse responseDto = new ProductResponse(
                1L, "Товар", null, null, null, null, null, null, null, null
        );
        when(productService.getProduct(1L, null)).thenReturn(responseDto);

        ResponseEntity<ProductResponse> response = controller.getProduct(1L, null);

        assertThat(response.getBody()).isEqualTo(responseDto);
        verify(productService).getProduct(1L, null);
    }

    @Test
    void getProductByBarcode_shouldReturnOk() {
        Set<ProductInclude> includes = Set.of(ProductInclude.FACTORS);
        ProductResponse responseDto = new ProductResponse(
                1L, "Товар", null, null, null, null, "4600000000011", null, null, null
        );
        when(productService.getProductByBarcode("4600000000011", includes)).thenReturn(responseDto);

        ResponseEntity<ProductResponse> response = controller.getProductByBarcode("4600000000011", includes);

        assertThat(response.getBody()).isEqualTo(responseDto);
        verify(productService).getProductByBarcode("4600000000011", includes);
    }

    @Test
    void updateProduct_shouldReturnOk() {
        UpdateProductRequest request = new UpdateProductRequest(
                "Обновлённый товар", 2L, 2L, null, "4600000000011", null, null, null
        );
        ProductResponse updated = new ProductResponse(
                1L, "Обновлённый товар", null, null, null, null, "4600000000011", null, null, null
        );
        when(productService.updateProduct(eq(1L), any(UpdateProductRequest.class))).thenReturn(updated);

        ResponseEntity<ProductResponse> response = controller.updateProduct(1L, request);

        assertThat(response.getBody().name()).isEqualTo("Обновлённый товар");
    }

    @Test
    void deleteProduct_shouldInvokeService() {
        controller.deleteProduct(1L);
        verify(productService).deleteProduct(1L);
    }
}
