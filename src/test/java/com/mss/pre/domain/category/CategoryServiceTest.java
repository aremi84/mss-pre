package com.mss.pre.domain.category;

import com.mss.pre.api.codishop.model.response.HighestLowestProductInfo;
import com.mss.pre.api.codishop.model.response.LowestByCategoryProductInfo;
import com.mss.pre.common.exception.ApiException;
import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.category.entity.CategoryAggregate;
import com.mss.pre.domain.category.repository.CategoryAggregateRepository;
import com.mss.pre.domain.category.repository.CategoryRepository;
import com.mss.pre.domain.category.service.CategoryService;
import com.mss.pre.domain.product.entity.Product;
import com.mss.pre.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryAggregateRepository aggregateRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Brand brand;
    private Category category;
    private Product product;
    private CategoryAggregate aggregate;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setBrandId(1L);
        brand.setBrandName("Test Brand");

        category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Test Category");

        product = new Product();
        product.setProductId(1L);
        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(100L);

        aggregate = new CategoryAggregate();
        aggregate.setCategoryId(category.getCategoryId());
        aggregate.setLowestProductId(1L);
        aggregate.setLowestPrice(100L);
        aggregate.setHighestProductId(2L);
        aggregate.setHighestPrice(200L);
    }

    @Test
    void 카테고리별_최저가_상품_정보를_조회한다() {
        when(aggregateRepository.findAll()).thenReturn(List.of(aggregate));
        when(productRepository.findAllById(List.of(1L))).thenReturn(List.of(product));

        LowestByCategoryProductInfo result = categoryService.getLowestProductByCategory();

        assertNotNull(result);
        assertEquals(100L, result.getTotalPrice());
        assertEquals(1, result.getProductInfo().size());
        assertEquals("Test Category", result.getProductInfo().get(0).getCategoryName());
    }

    @Test
    void 카테고리별_최저가_상품_정보가_없으면_null을_반환한다() {
        when(aggregateRepository.findAll()).thenReturn(List.of());

        LowestByCategoryProductInfo result = categoryService.getLowestProductByCategory();

        assertNull(result);
    }

    @Test
    void 카테고리_이름으로_최저_최고_가격_상품_정보를_조회한다() {
        when(categoryRepository.findByCategoryName("Test Category")).thenReturn(Optional.of(category));
        when(aggregateRepository.findById(1L)).thenReturn(Optional.of(aggregate));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        HighestLowestProductInfo result = categoryService.getLowestHighestProductByCategoryName("Test Category");

        assertNotNull(result);
        assertEquals("Test Category", result.getCategoryName());
        assertEquals(100L, result.getLowestProduct().getPrice());
        assertEquals(100L, result.getHighestProduct().getPrice());
    }

    @Test
    void 카테고리_이름으로_조회시_카테고리가_없으면_예외를_던진다() {
        when(categoryRepository.findByCategoryName("Test Category")).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> categoryService.getLowestHighestProductByCategoryName("Test Category"));
        assertEquals("카테고리가 존재하지 않습니다. categoryName : Test Category", exception.getMessage());
    }

    @Test
    void 카테고리_이름으로_조회시_집계_정보가_없으면_null을_반환한다() {
        when(categoryRepository.findByCategoryName("Test Category")).thenReturn(Optional.of(category));
        when(aggregateRepository.findById(1L)).thenReturn(Optional.empty());

        HighestLowestProductInfo result = categoryService.getLowestHighestProductByCategoryName("Test Category");

        assertNull(result);
    }
}