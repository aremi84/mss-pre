package com.mss.pre.api.codishop.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 카테고리 별 최저가격 상품 정보 반환 모델
 */
@Getter
@Setter
@Builder
public class LowestByCategoryProductInfo {
    @JsonProperty("최저가상품")
    private List<ProductInfo> productInfo;

    @JsonProperty("총합")
    private Long totalPrice;

    @Getter
    @Setter
    @Builder
    public static class ProductInfo {
        @JsonProperty("브랜드")
        public String brandName;

        @JsonProperty("카테고리")
        public String categoryName;

        @JsonProperty("가격")
        public long price;
    }
}
