package com.mss.pre.domain.category.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 상품 변경 이벤트 메시지 모델
 */
@Getter
@AllArgsConstructor
public class EventMessage {
    private long productId;
}
