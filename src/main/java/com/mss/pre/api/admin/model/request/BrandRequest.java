package com.mss.pre.api.admin.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 브랜드 등록 요청 모델
 */
@Getter
@Setter
public class BrandRequest {

    @NotBlank(message = "브랜드명을 입력해주세요")
    @Size(min = 1, max = 50)
    private String name;
}
