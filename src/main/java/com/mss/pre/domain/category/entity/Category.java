package com.mss.pre.domain.category.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * CATEGORY 테이블 엔티티
 */
@Entity
@Table(name = "CATEGORY")
@NoArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID", nullable = false)
    @Comment(value = "카테고리 아이디")
    private long categoryId;

    @Column(name = "CATEGORY_NAME", nullable = false)
    @Comment(value = "카테고리명")
    private String categoryName;
}
