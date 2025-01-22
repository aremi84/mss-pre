package com.mss.pre.domain.brand.repository;

import com.mss.pre.domain.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findAllByDeleteYnFalse();
    Optional<Brand> findByBrandIdAndDeleteYnFalse(long brandId);
    Optional<Brand> findByBrandName(String brandName);
}