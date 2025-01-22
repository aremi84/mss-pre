package com.mss.pre.api.admin.controller;

import com.mss.pre.api.admin.model.request.ProductRequest;
import com.mss.pre.domain.product.service.ProductAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자용 상품 관리 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class ProductAdminController {
    private final ProductAdminService productAdminService;

    /**
     * 상품을 생성한다.
     */
    @PostMapping
    public void create(@RequestBody @Valid ProductRequest request) {
        productAdminService.create(request);
    }

    /**
     * 상품을 수정한다.
     */
    @PutMapping("/{productId}")
    public void update(@PathVariable long productId, @RequestBody @Valid ProductRequest request) {
        productAdminService.update(productId, request);
    }

    /**
     * 상품을 삭제한다.
     * - 삭제 여부 플래그를 변경한다.(DELETE_YN)
     */
    @DeleteMapping("/{productId}")
    public void delete(@PathVariable long productId) {
        productAdminService.delete(productId);
    }
}
