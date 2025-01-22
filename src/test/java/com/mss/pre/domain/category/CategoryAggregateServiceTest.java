package com.mss.pre.domain.category;

import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.category.entity.CategoryAggregate;
import com.mss.pre.domain.category.repository.CategoryAggregateRepository;
import com.mss.pre.domain.category.repository.CategoryRepository;
import com.mss.pre.domain.category.service.CategoryAggregateService;
import com.mss.pre.domain.product.entity.Product;
import com.mss.pre.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryAggregateServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryAggregateRepository aggregateRepository;

    @InjectMocks
    private CategoryAggregateService categoryAggregateService;

    private Category category;
    private Product product;
    private CategoryAggregate aggregate;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1L);

        product = new Product();
        product.setProductId(1L);
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
    void 카테고리_목록이_비어있으면_초기화하지_않는다() {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        categoryAggregateService.init();

        verify(aggregateRepository, never()).saveAll(anyList());
    }

    @Test
    void 카테고리별_최저가_최고가_상품_정보를_집계한다() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(productRepository.findTopByCategoryAndDeleteYnFalseOrderByPriceAsc(category)).thenReturn(product);
        when(productRepository.findTopByCategoryAndDeleteYnFalseOrderByPriceDesc(category)).thenReturn(product);

        categoryAggregateService.init();

        verify(aggregateRepository).saveAll(argThat(aggregates -> {
            if (!aggregates.iterator().hasNext()) {
                return false;
            }
            CategoryAggregate aggregate = ((List<CategoryAggregate>) aggregates).get(0);
            return aggregate.getCategoryId() == category.getCategoryId()
                    && aggregate.getLowestProductId() == product.getProductId()
                    && aggregate.getLowestPrice() == product.getPrice()
                    && aggregate.getHighestProductId() == product.getProductId()
                    && aggregate.getHighestPrice() == product.getPrice();
        }));
    }

    @Test
    void 상품이_존재하지_않으면_갱신하지_않는다() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        categoryAggregateService.update(1L);

        verify(aggregateRepository, never()).save(any());
        verify(aggregateRepository, never()).deleteById(anyLong());
    }

    @Test
    void 카테고리_상품이_모두_삭제되면_집계_테이블에서_삭제한다() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.countByCategoryAndDeleteYnFalse(category)).thenReturn(0L);

        categoryAggregateService.update(1L);

        verify(aggregateRepository).deleteById(category.getCategoryId());
    }

    @Test
    void 갱신대상_조건을_만족하면_집계_테이블을_갱신한다() {
        product.setPrice(50L); // 최저가 설정

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.countByCategoryAndDeleteYnFalse(category)).thenReturn(1L);
        when(aggregateRepository.findById(category.getCategoryId())).thenReturn(Optional.of(aggregate));
        when(productRepository.findTopByCategoryAndDeleteYnFalseOrderByPriceAsc(category)).thenReturn(product);
        when(productRepository.findTopByCategoryAndDeleteYnFalseOrderByPriceDesc(category)).thenReturn(product);

        categoryAggregateService.update(1L);

        verify(aggregateRepository).save(any(CategoryAggregate.class));
    }

    @Test
    void 갱신대상_조건을_만족하지_않으면_집계_테이블을_갱신하지_않는다() {
        product.setPrice(150L); // 갱신 대상 가격에 포함되지 않음

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.countByCategoryAndDeleteYnFalse(category)).thenReturn(1L);
        when(aggregateRepository.findById(category.getCategoryId())).thenReturn(Optional.of(aggregate));

        categoryAggregateService.update(1L);

        verify(aggregateRepository, never()).save(any(CategoryAggregate.class));
    }
}
