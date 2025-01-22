package com.mss.pre.api.codishop.controller;

import com.mss.pre.api.codishop.model.response.HighestLowestProductInfo;
import com.mss.pre.api.codishop.model.response.LowestByBrandProductInfo;
import com.mss.pre.api.codishop.model.response.LowestByCategoryProductInfo;
import com.mss.pre.domain.brand.service.BrandService;
import com.mss.pre.domain.category.service.CategoryService;
import com.mss.pre.web.model.BrandInfo;
import com.mss.pre.web.model.CategoryInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 사용자용 코디샵 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/codishop")
public class CodiShopController {

    private final BrandService brandService;
    private final CategoryService categoryService;

    /**
     * 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회한다.
     */
    @GetMapping("/category/lowest-prices")
    public LowestByCategoryProductInfo getLowestProductInfoByCategory() {
        return categoryService.getLowestProductByCategory();
    }

    /**
     * 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회한다.
     */
    @GetMapping("/category/lowest-highest-price/{categoryName}")
    public HighestLowestProductInfo getProductInfoByCategoryName(@PathVariable("categoryName") String categoryName) {
        return categoryService.getLowestHighestProductByCategoryName(categoryName);
    }

    /**
     * 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회한다.
     */
    @GetMapping("/brand/lowest-price")
    public LowestByBrandProductInfo getLowestProductInfoByBrand() {
        return brandService.getLowestProductInfoByBrand();
    }

    /**
     * 카테고리 정보를 조회한다. (프론트 호출용)
     * @return
     */
    @GetMapping("/category")
    public List<CategoryInfo> getCategory() {
        return categoryService.getCategoryInfo();
    }

    /**
     * 브랜드 정보를 조회한다. (프론트 호출용)
     * @return
     */
    @GetMapping("/brand")
    public List<BrandInfo> getBrand() {
        return brandService.getBrandInfo();
    }
}
