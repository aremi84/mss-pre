package com.mss.pre.domain.brand.service;

import com.mss.pre.api.codishop.model.response.LowestByBrandProductInfo;
import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.brand.repository.BrandRepository;
import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.product.entity.Product;
import com.mss.pre.domain.product.repository.ProductRepository;
import com.mss.pre.web.model.BrandInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    /**
     * 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 단일 브랜드와 카테고리의 상품가격, 총액을 조회한다.
     * 1. 모든 브랜드를 조회한다.
     * 2. 각 브랜드별로 삭제되지 않은 상품을 조회하고, 카테고리별 최저가 상품을 선정한다.
     * 3. 각 브랜드의 전체 카테고리 상품 합산가를 계산한다.
     * 4. 전체 상품 합산가가 가장 낮은 브랜드를 선정한다.
     * 5. 최저가 브랜드의 상품 정보와 총액을 반환한다.
     * @return
     */
    public LowestByBrandProductInfo getLowestProductInfoByBrand() {

        // 1. 모든 브랜드 정보를 조회한다.
        List<Brand> brandList = brandRepository.findAll();
        if (CollectionUtils.isEmpty(brandList)) {
            return null;
        }

        // 2. 브랜드 목록을 순차하면서 카테고리별 최저가 상품을 선정한다.
        Map<Long, Map<Category, Product>> brandProductMap = new HashMap<>();
        for (Brand brand : brandList) {
            List<Product> productList = productRepository.findByBrandAndDeleteYnFalse(brand);
            if (CollectionUtils.isEmpty(productList)) {
                continue;
            }

            // 동일한 카테고리 아이디를 가진 여러개의 상품이 존재할 수 있다. 이 경우 가격이 낮은 상품을 선정한다.
            Map<Category, Product> categoryProductMap = productList.stream().collect(
                    Collectors.toMap(
                            Product::getCategory,
                            Function.identity(),
                            (a, b) -> a.getPrice() <= b.getPrice() ? a : b
            ));

            // 전체 카테고리 상품 합산가를 계산하고 맵에 저장한다.
            Long totalPrice = categoryProductMap.values().stream().mapToLong(Product::getPrice).sum();
            brandProductMap.put(totalPrice, categoryProductMap);
        }

        // 3. 전체 상품 합산가가 가장 낮은 브랜드를 선정한다.
        Optional<Map<Long, Map<Category, Product>>> lowestBrandProductMap = brandProductMap.entrySet().stream()
                .min(Map.Entry.comparingByKey())
                .map(entry -> Collections.singletonMap(entry.getKey(), entry.getValue()));

        // 4. 최종 반환 모델 형식으로 변환한다.
        if (lowestBrandProductMap.isPresent()) {
            Map.Entry<Long, Map<Category, Product>> entry = lowestBrandProductMap.get().entrySet().iterator().next();
            Map<Category, Product> categoryProductMap = entry.getValue();

            // 상품 정보
            LowestByBrandProductInfo.ProductInfo productInfo = LowestByBrandProductInfo.ProductInfo.builder()
                    .totalPrice(entry.getKey())
                    .brandName(categoryProductMap.values().stream().findFirst().get().getBrand().getBrandName())
                    .build();

            // 카테고리 정보
            List<LowestByBrandProductInfo.CategoryInfo> categoryInfoList = categoryProductMap.values().stream()
                .map(product -> LowestByBrandProductInfo.CategoryInfo.builder()
                    .category(product.getCategory().getCategoryName())
                    .price(product.getPrice())
                    .build())
                .collect(Collectors.toList());
            productInfo.setCategoryInfo(categoryInfoList);

            LowestByBrandProductInfo lowestByBrandProductInfo = new LowestByBrandProductInfo();
            lowestByBrandProductInfo.setProductInfo(productInfo);
            return lowestByBrandProductInfo;
        }
        return null;
    }

    /**
     * 삭제되지 않은 모든 브랜드 정보를 조회한다.
     * @return
     */
    public List<BrandInfo> getBrandInfo() {
        List<Brand> brands = brandRepository.findAllByDeleteYnFalse();
        return brands.stream().map(brand -> BrandInfo.builder()
                .brandId(brand.getBrandId())
                .brandName(brand.getBrandName())
                .build())
                .collect(Collectors.toList());
    }
}
