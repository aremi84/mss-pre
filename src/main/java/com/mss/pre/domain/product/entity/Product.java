package com.mss.pre.domain.product.entity;

import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

/**
 * PRODUCT 테이블 엔티티
 */
@Entity
@Table(name = "PRODUCT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product {

    @Id
    @Column(name = "PRODUCT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRAND_ID", nullable = false)
    @Comment(value = "브랜드 아이디")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    @Comment(value = "카테고리 아이디")
    private Category category;

    @Column(name = "PRICE", nullable = false)
    @Comment(value = "가격")
    private long price;

    @Column(name = "DELETE_YN", nullable = false)
    @Comment(value = "삭제여부")
    private boolean deleteYn = false;
}
