package com.mss.pre.domain.category.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AggregateEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 상품 변경 이벤트를 발행한다.
     * @param productId
     */
    @Transactional
    public void sendEvent(long productId) {
        applicationEventPublisher.publishEvent(new EventMessage(productId));
    }
}
