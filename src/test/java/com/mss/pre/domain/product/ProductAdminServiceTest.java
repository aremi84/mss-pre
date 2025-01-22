package com.mss.pre.domain.product;

import com.mss.pre.api.admin.model.request.ProductRequest;
import com.mss.pre.common.exception.ApiException;
import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.brand.service.BrandAdminService;
import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.category.event.AggregateEventPublisher;
import com.mss.pre.domain.category.service.CategoryAdminService;
import com.mss.pre.domain.product.entity.Product;
import com.mss.pre.domain.product.repository.ProductRepository;
import com.mss.pre.domain.product.service.ProductAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAdminServiceTest {

    @Mock
    private BrandAdminService brandAdminService;

    @Mock
    private CategoryAdminService categoryAdminService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AggregateEventPublisher aggregateEventPublisher;

    @InjectMocks
    private ProductAdminService productAdminService;

    private ProductRequest productRequest;
    private Brand brand;
    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setBrandId(1L);

        category = new Category();
        category.setCategoryId(1L);

        productRequest = new ProductRequest();
        productRequest.setBrandId(1L);
        productRequest.setCategoryId(1L);
        productRequest.setPrice(100L);

        product = Product.builder()
                .brand(brand)
                .category(category)
                .price(100L)
                .build();
        product.setProductId(1L);
    }

    @Test
    void 상품을_생성한다() {
        when(brandAdminService.findById(1L)).thenReturn(brand);
        when(categoryAdminService.findById(1L)).thenReturn(category);

        productAdminService.create(productRequest);

        verify(productRepository).save(any(Product.class));
        verify(aggregateEventPublisher).sendEvent(anyLong());
    }

    @Test
    void 상품을_수정한다() {
        when(productRepository.findByProductIdAndDeleteYnFalse(1L)).thenReturn(Optional.of(product));
        when(brandAdminService.findById(1L)).thenReturn(brand);
        when(categoryAdminService.findById(1L)).thenReturn(category);

        productAdminService.update(1L, productRequest);

        verify(productRepository).save(any(Product.class));
        verify(aggregateEventPublisher).sendEvent(anyLong());
    }

    @Test
    void 상품을_삭제한다() {
        when(productRepository.findByProductIdAndDeleteYnFalse(1L)).thenReturn(Optional.of(product));

        productAdminService.delete(1L);

        assertTrue(product.isDeleteYn());
        verify(productRepository).save(any(Product.class));
        verify(aggregateEventPublisher).sendEvent(anyLong());
    }

    @Test
    void 상품이_존재하지_않으면_예외를_던진다() {
        when(productRepository.findByProductIdAndDeleteYnFalse(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> productAdminService.update(1L, productRequest));
        assertEquals("상품이 존재하지 않습니다. productId : 1", exception.getMessage());
    }
}
