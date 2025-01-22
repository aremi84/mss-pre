package com.mss.pre.domain.category.service;

import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.category.entity.CategoryAggregate;
import com.mss.pre.domain.category.repository.CategoryAggregateRepository;
import com.mss.pre.domain.category.repository.CategoryRepository;
import com.mss.pre.domain.product.entity.Product;
import com.mss.pre.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryAggregateService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryAggregateRepository aggregateRepository;

    /**
     * 카테고리별 최저,최고가 상품 정보를 집계하여 테이블에 저장한다.(AGGREGATE_BY_PRICE)
     */
    @Transactional
    public void init() {
        List<Category> categoryList = categoryRepository.findAll();
        if (CollectionUtils.isEmpty(categoryList)) {
            return;
        }

        List<CategoryAggregate> aggregates = new ArrayList<>();
        categoryList.forEach(category -> {
            Product lowestProduct = productRepository.findTopByCategoryAndDeleteYnFalseOrderByPriceAsc(category);
            Product highestProduct = productRepository.findTopByCategoryAndDeleteYnFalseOrderByPriceDesc(category);
            if (Objects.nonNull(lowestProduct) && Objects.nonNull(highestProduct)) {
                aggregates.add(CategoryAggregate.builder()
                        .categoryId(category.getCategoryId())
                        .lowestProductId(lowestProduct.getProductId())
                        .lowestPrice(lowestProduct.getPrice())
                        .highestProductId(highestProduct.getProductId())
                        .highestPrice(highestProduct.getPrice())
                        .build());
            }
        });
        aggregateRepository.saveAll(aggregates);
    }

    /**
     * 상품 정보가 변경되었을 때 집계 테이블을 갱신한다.
     * @param productId
     */
    @Transactional
    public void update(long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return;
        }

        Product product = optionalProduct.get();
        Category category = product.getCategory();
        // 카테고리 상품이 모두 삭제되었다면 집계 테이블에 저장된 데이터를 삭제한다.
        long count = productRepository.countByCategoryAndDeleteYnFalse(category);
        if (count == 0) {
            aggregateRepository.deleteById(category.getCategoryId());
            return;
        }

        aggregateRepository.findById(category.getCategoryId()).ifPresent(
                aggregate -> {
                    if (isUpdateTarget(aggregate, product)) {
                        Product lowestProduct = productRepository.findTopByCategoryAndDeleteYnFalseOrderByPriceAsc(category);
                        Product highestProduct = productRepository.findTopByCategoryAndDeleteYnFalseOrderByPriceDesc(category);
                        if (Objects.nonNull(lowestProduct) && Objects.nonNull(highestProduct)) {
                            aggregate.setLowestProductId(lowestProduct.getProductId());
                            aggregate.setLowestPrice(lowestProduct.getPrice());
                            aggregate.setHighestProductId(highestProduct.getProductId());
                            aggregate.setHighestPrice(highestProduct.getPrice());
                            aggregateRepository.save(aggregate);
                        }
                    }
                }
        );
    }

    /**
     * 집계 테이블 갱신 대상인지 확인한다.
     * - 집계 테이블에 저장된 상품이 삭제된 경우
     * - 가격이 변경된 상품이 최저가, 최고가 상품인 경우
     * @param aggregate 현재 집계 데이터
     * @param product 변경된 상품 데이터
     * @return 갱신 대상 여부
     */
    private boolean isUpdateTarget(CategoryAggregate aggregate, Product product) {
        long productId = product.getProductId();
        long productPrice = product.getPrice();
        boolean isDeletedProduct = product.isDeleteYn();
        boolean isLowestProduct = aggregate.getLowestProductId() == productId;
        boolean isHighestProduct = aggregate.getHighestProductId() == productId;
        boolean isPriceOutRange = productPrice < aggregate.getLowestPrice() || productPrice > aggregate.getHighestPrice();

        return (isLowestProduct && isDeletedProduct) || (isHighestProduct && isDeletedProduct) || isPriceOutRange;
    }
}
