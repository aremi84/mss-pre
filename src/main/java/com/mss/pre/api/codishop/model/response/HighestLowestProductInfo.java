package com.mss.pre.api.codishop.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 카테고리 기준 최저, 최고가 상품 반환 모델
 */
@Getter
@Setter
@Builder
public class HighestLowestProductInfo {
    @JsonProperty("카테고리")
    String categoryName;

    @JsonProperty("최저가")
    ProductInfo lowestProduct;

    @JsonProperty("최고가")
    ProductInfo highestProduct;

    @Getter
    @Setter
    @Builder
    public static class ProductInfo {
        @JsonProperty("브랜드")
        private String brandName;

        @JsonProperty("가격")
        private long price;
    }
}
