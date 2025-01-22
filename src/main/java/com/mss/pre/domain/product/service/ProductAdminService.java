package com.mss.pre.domain.product.service;

import com.mss.pre.api.admin.model.request.ProductRequest;
import com.mss.pre.common.exception.ApiException;
import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.brand.service.BrandAdminService;
import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.category.event.AggregateEventPublisher;
import com.mss.pre.domain.category.service.CategoryAdminService;
import com.mss.pre.domain.product.entity.Product;
import com.mss.pre.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 관리자용 상품 관리 서비스
 */
@Service
@RequiredArgsConstructor
public class ProductAdminService {

    private final BrandAdminService brandAdminService;
    private final CategoryAdminService categoryAdminService;
    private final ProductRepository productRepository;
    private final AggregateEventPublisher aggregateEventPublisher;

    /**
     * 상품을 생성한다.
     * @param request
     */
    @Transactional
    public void create(ProductRequest request) {
        Brand brand = brandAdminService.findById(request.getBrandId());
        Category category = categoryAdminService.findById(request.getCategoryId());

        Product product = Product.builder()
                .brand(brand)
                .category(category)
                .price(request.getPrice()).build();

        productRepository.save(product);

        aggregateEventPublisher.sendEvent(product.getProductId());
    }

    /**
     * 상품을 수정한다.
     * @param productId
     * @param request
     */
    @Transactional
    public void update(long productId, ProductRequest request) {
        Product product = findById(productId);
        Brand brand = brandAdminService.findById(request.getBrandId());
        Category category = categoryAdminService.findById(request.getCategoryId());

        product.setPrice(request.getPrice());
        product.setBrand(brand);
        product.setCategory(category);

        productRepository.save(product);

        aggregateEventPublisher.sendEvent(product.getProductId());
    }

    /**
     * 상품을 삭제한다.
     * @param productId
     */
    @Transactional
    public void delete(long productId) {
        Product product = findById(productId);
        product.setDeleteYn(true);

        productRepository.save(product);

        aggregateEventPublisher.sendEvent(product.getProductId());
    }

    /**
     * 삭제되지 않은 상품을 조회한다.
     * @param productId
     * @return
     */
    private Product findById(long productId) {
        return productRepository.findByProductIdAndDeleteYnFalse(productId)
                .orElseThrow(() -> new ApiException("상품이 존재하지 않습니다. productId : " + productId));
    }
}
