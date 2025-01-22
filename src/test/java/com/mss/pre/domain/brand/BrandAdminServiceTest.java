package com.mss.pre.domain.brand;

import com.mss.pre.api.admin.model.request.BrandRequest;
import com.mss.pre.common.exception.ApiException;
import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.brand.repository.BrandRepository;
import com.mss.pre.domain.brand.service.BrandAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandAdminServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandAdminService brandAdminService;

    @Test
    void 새로운_브랜드를_생성한다() {
        BrandRequest request = new BrandRequest();
        request.setName("New Brand");

        when(brandRepository.findByBrandName(request.getName())).thenReturn(Optional.empty());

        brandAdminService.create(request);

        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void 브랜드명이_중복되어_생성에_실패한다() {
        BrandRequest request = new BrandRequest();
        request.setName("Existing Brand");

        when(brandRepository.findByBrandName(request.getName())).thenReturn(Optional.of(new Brand()));

        ApiException exception = assertThrows(ApiException.class, () -> brandAdminService.create(request));
        assertEquals("이미 등록된 브랜드입니다", exception.getMessage());
    }
}