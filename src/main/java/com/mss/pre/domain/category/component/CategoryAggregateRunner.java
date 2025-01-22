package com.mss.pre.domain.category.component;

import com.mss.pre.domain.category.service.CategoryAggregateService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 카테고리별 최저,최고가 상품 정보를 집계하여 테이블에 저장 (AGGREGATE_BY_PRICE)
 * 애플리케이션 구동 완료 후 실행된다.
 */
@Component
@RequiredArgsConstructor
public class CategoryAggregateRunner implements ApplicationRunner {

    private final CategoryAggregateService categoryAggregateService;

    @Override
    public void run(ApplicationArguments args) {
        categoryAggregateService.init();
    }
}
