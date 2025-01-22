package com.mss.pre.domain.brand.service;

import com.mss.pre.api.admin.model.request.BrandRequest;
import com.mss.pre.common.exception.ApiException;
import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.brand.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BrandAdminService {

    private final BrandRepository brandRepository;

    /**
     * 브랜드를 생성한다.
     * @param request
     */
    @Transactional
    public void create(BrandRequest request) {
        if (brandRepository.findByBrandName(request.getName()).isPresent()) {
            throw new ApiException("이미 등록된 브랜드입니다");
        }

        Brand brand = Brand.builder().brandName(request.getName()).build();
        brandRepository.save(brand);
    }

    /**
     * 브랜드를 수정한다.
     * @param brandId
     * @param request
     */
    @Transactional
    public void update(long brandId, BrandRequest request) {
        Brand brand = findById(brandId);
        brand.setBrandName(request.getName());
        brandRepository.save(brand);
    }

    /**
     * 브랜드를 삭제한다.
     * @param brandId
     */
    @Transactional
    public void delete(long brandId) {
        Brand brand = findById(brandId);
        brand.setDeleteYn(true);
        brandRepository.save(brand);
    }

    /**
     * 삭제되지 않은 브랜드 정보를 조회한다
     */
    public Brand findById(long brandId) {
        return brandRepository.findByBrandIdAndDeleteYnFalse(brandId)
                .orElseThrow(() -> new ApiException("브랜드가 존재하지 않습니다. brandId : " + brandId));
    }
}
