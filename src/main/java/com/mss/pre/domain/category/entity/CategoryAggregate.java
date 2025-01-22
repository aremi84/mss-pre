package com.mss.pre.domain.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Comment;

/**
 * AGGREGATE_BY_PRICE 테이블 엔티티
 */
@Entity
@Table(name = "AGGREGATE_BY_PRICE")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAggregate {

    @Id
    @Column(name = "CATEGORY_ID", nullable = false)
    @Comment(value = "카테고리 아이디")
    private long categoryId;

    @Column(name = "LOWEST_PRODUCT_ID", nullable = false)
    @Comment(value = "최저가 상품 아이디")
    private long lowestProductId;

    @Column(name = "LOWEST_PRICE", nullable = false)
    @Comment(value = "최저가")
    private long lowestPrice;

    @Column(name = "HIGHEST_PRODUCT_ID", nullable = false)
    @Comment(value = "최고가 상품 아이디")
    private long highestProductId;

    @Column(name = "HIGHEST_PRICE", nullable = false)
    @Comment(value = "최고가")
    private long highestPrice;
}
