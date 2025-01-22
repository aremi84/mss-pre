package com.mss.pre.domain.category;

import com.mss.pre.common.exception.ApiException;
import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.category.repository.CategoryRepository;
import com.mss.pre.domain.category.service.CategoryAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryAdminServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryAdminService categoryAdminService;

    @Test
    void 카테고리_정보를_조회한다() {
        long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName("Category Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryAdminService.findById(categoryId);

        assertNotNull(result);
        assertEquals(categoryId, result.getCategoryId());
        assertEquals("Category Name", result.getCategoryName());
    }

    @Test
    void 카테고리가_존재하지_않으면_예외를_던진다() {
        long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> categoryAdminService.findById(categoryId));
        assertEquals("카테고리가 존재하지 않습니다. categoryId : " + categoryId, exception.getMessage());
    }
}
