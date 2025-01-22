package com.mss.pre.domain.brand.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

/**
 * BRAND 테이블 엔티티
 */
@Entity
@Table(name = "BRAND")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRAND_ID", nullable = false)
    @Comment(value = "브랜드 아이디")
    private long brandId;

    @Column(name = "BRAND_NAME", nullable = false)
    @Comment(value = "브랜드명")
    private String brandName;

    @Column(name = "DELETE_YN", nullable = false)
    @Comment(value = "삭제여부")
    @Builder.Default
    private boolean deleteYn = false;
}
