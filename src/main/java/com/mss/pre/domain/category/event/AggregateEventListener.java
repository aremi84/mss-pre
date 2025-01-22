package com.mss.pre.domain.category.event;

import com.mss.pre.domain.category.service.CategoryAggregateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AggregateEventListener {

    private final CategoryAggregateService categoryAggregateService;

    /**
     * 상품 변경 이벤트를 수신하여 집계 테이블을 갱신한다.
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMessage(EventMessage message) {
        categoryAggregateService.update(message.getProductId());
    }
}
