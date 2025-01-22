package com.mss.pre.domain.category.service;

import com.mss.pre.api.codishop.model.response.HighestLowestProductInfo;
import com.mss.pre.api.codishop.model.response.LowestByCategoryProductInfo;
import com.mss.pre.common.exception.ApiException;
import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.category.entity.CategoryAggregate;
import com.mss.pre.domain.category.repository.CategoryAggregateRepository;
import com.mss.pre.domain.category.repository.CategoryRepository;
import com.mss.pre.domain.product.entity.Product;
import com.mss.pre.domain.product.repository.ProductRepository;
import com.mss.pre.web.model.CategoryInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryAggregateRepository aggregateRepository;

    /**
     * 카테고리별 최저가격 브랜드와 상품 가격, 총액을 조회한다.
     * @return
     */
    public LowestByCategoryProductInfo getLowestProductByCategory() {

        // 1. 카테고리별 최저가, 최고가 집계 테이블에 저장된 데이터를 조회한다.
        List<CategoryAggregate> aggregates = aggregateRepository.findAll();
        if (CollectionUtils.isEmpty(aggregates)) {
            return null;
        }

        // 2. 집계 테이블에 저장된 상품 아이디로 모든 상품 정보를 조회한다.
        List<Product> productList = productRepository.findAllById(aggregates.stream().map(CategoryAggregate::getLowestProductId).toList());
        Map<Long, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));

        // 3. 최저가 상품 정보를 추출하여 리스트로 변환한다.
        List<LowestByCategoryProductInfo.ProductInfo> lowestProduct = aggregates.stream()
                .map(aggregate -> MapUtils.getObject(productMap, aggregate.getLowestProductId(), null))
                .filter(Objects::nonNull)
                .map(product -> LowestByCategoryProductInfo.ProductInfo.builder()
                        .categoryName(product.getCategory().getCategoryName())
                        .brandName(product.getBrand().getBrandName())
                        .price(product.getPrice())
                        .build())
                .collect(Collectors.toList());

        // 4. 최저가 상품 정보와 총 가격을 포함한 객체를 반환한다.
        return LowestByCategoryProductInfo.builder()
                .totalPrice(productList.stream().mapToLong(Product::getPrice).sum())
                .productInfo(lowestProduct)
                .build();
    }

    /**
     * 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회한다.
     * @param categoryName
     * @return
     */
    public HighestLowestProductInfo getLowestHighestProductByCategoryName(String categoryName) {

        // 1. 카테고리 정보를 조회한다.
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ApiException("카테고리가 존재하지 않습니다. categoryName : " + categoryName));

        // 2. 집계 테이블에서 카테고리에 해당하는 정보를 조회한다.
        CategoryAggregate aggregate = aggregateRepository.findById(category.getCategoryId()).orElse(null);
        if (Objects.isNull(aggregate)) {
            return null;
        }

        // 3. 반환할 객체를 생성하고 상품 테이블에서 최저가, 최고가 상품 정보를 조회하여 셋팅한다.
        HighestLowestProductInfo highestLowestProductInfo = HighestLowestProductInfo.builder().categoryName(categoryName).build();

        Optional<Product> lowestProduct = productRepository.findById(aggregate.getLowestProductId());
        lowestProduct.ifPresent(product -> highestLowestProductInfo.setLowestProduct(
                HighestLowestProductInfo.ProductInfo.builder()
                        .brandName(product.getBrand().getBrandName())
                        .price(product.getPrice())
                        .build())
        );

        Optional<Product> highestProduct = productRepository.findById(aggregate.getHighestProductId());
        highestProduct.ifPresent(product -> highestLowestProductInfo.setHighestProduct(
                HighestLowestProductInfo.ProductInfo.builder()
                        .brandName(product.getBrand().getBrandName())
                        .price(product.getPrice())
                        .build())
        );

        return highestLowestProductInfo;
    }

    /**
     * 모든 카테고리 정보를 조회한다.
     * @return
     */
    public List<CategoryInfo> getCategoryInfo() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> CategoryInfo.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build())
                .collect(Collectors.toList());
    }
}
