package com.mss.pre.domain.category.service;

import com.mss.pre.common.exception.ApiException;
import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryAdminService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 정보를 조회한다
     */
    public Category findById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException("카테고리가 존재하지 않습니다. categoryId : " + categoryId));
    }
}
