package com.mss.pre.api.codishop.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 브랜드 기준 최저 가격 상품 정보 반환 모델
 */
@Getter
@Setter
public class LowestByBrandProductInfo {

    @JsonProperty("최저가")
    private ProductInfo ProductInfo;

    @Getter
    @Setter
    @Builder
    static public class ProductInfo {
        @JsonProperty("브랜드")
        private String brandName;

        @JsonProperty("총액")
        private long totalPrice;

        @JsonProperty("카테고리")
        private List<CategoryInfo> categoryInfo;
    }

    @Getter
    @Setter
    @Builder
    static public class CategoryInfo {
        @JsonProperty("카테고리")
        private String category;

        @JsonProperty("가격")
        private long price;
    }
}
