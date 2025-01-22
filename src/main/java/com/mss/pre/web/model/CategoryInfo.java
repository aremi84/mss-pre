package com.mss.pre.web.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryInfo {
    private long categoryId;
    private String categoryName;
}
