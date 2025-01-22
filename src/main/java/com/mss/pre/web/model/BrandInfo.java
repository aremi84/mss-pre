package com.mss.pre.web.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandInfo {
    private long brandId;
    private String brandName;
}
