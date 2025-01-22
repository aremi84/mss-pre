package com.mss.pre.domain.product.repository;

import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findTopByCategoryAndDeleteYnFalseOrderByPriceAsc(Category category);
    Product findTopByCategoryAndDeleteYnFalseOrderByPriceDesc(Category category);
    List<Product> findByBrandAndDeleteYnFalse(Brand brand);
    Optional<Product> findByProductIdAndDeleteYnFalse(long productId);
    long countByCategoryAndDeleteYnFalse(Category category);
}