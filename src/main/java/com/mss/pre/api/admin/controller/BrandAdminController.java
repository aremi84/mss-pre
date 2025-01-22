package com.mss.pre.api.admin.controller;

import com.mss.pre.api.admin.model.request.BrandRequest;
import com.mss.pre.domain.brand.service.BrandAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자용 브랜드 관리 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/brand")
public class BrandAdminController {

    private final BrandAdminService brandAdminService;

    /**
     * 브랜드를 생성한다.
     */
    @PostMapping
    public void create(@RequestBody @Valid BrandRequest request) {
        brandAdminService.create(request);
    }

    /**
     * 브랜드를 수정한다.
     */
    @PutMapping("/{brandId}")
    public void update(@PathVariable long brandId, @RequestBody @Valid BrandRequest request) {
        brandAdminService.update(brandId, request);
    }

    /**
     * 브랜드를 삭제한다.
     * - 삭제 여부 플래그를 변경한다.(DELETE_YN)
     * - 브랜드가 삭제되어도 해당 브랜드를 가진 상품은 삭제되지 않는다.
     */
    @DeleteMapping("/{brandId}")
    public void delete(@PathVariable long brandId) {
        brandAdminService.delete(brandId);
    }
}
