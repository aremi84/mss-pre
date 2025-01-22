package com.mss.pre.domain.category.repository;

import com.mss.pre.domain.category.entity.CategoryAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryAggregateRepository extends JpaRepository<CategoryAggregate, Long> {
}