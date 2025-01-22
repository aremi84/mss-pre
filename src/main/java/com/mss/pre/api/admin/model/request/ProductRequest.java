package com.mss.pre.api.admin.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 등록 요청 모델
 */
@Getter
@Setter
public class ProductRequest {

    @NotNull(message = "브랜드 아이디를 입력해주세요")
    private Long brandId;

    @NotNull(message = "카테고리 아이디를 입d력해주세요")
    private Long categoryId;

    @NotNull(message = "가격을 입력해주세요")
    @Min(value = 1, message = "가격은 1원 이상이어야 합니다")
    private Long price;
}
