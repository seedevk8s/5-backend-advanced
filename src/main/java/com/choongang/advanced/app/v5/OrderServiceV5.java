package com.choongang.advanced.app.v5;

import com.choongang.advanced.trace.logtrace.LogTrace;
import com.choongang.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV5 {

    private final OrderRepositoryV5 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {

        // 템플릿 메서드 패턴을 사용하여 비즈니스 로직을 실행합니다.
        AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {
            @Override
            protected Void call() {
                orderRepository.save(itemId);
                return null;
            }
        };
        template.execute("OrderServiceV4.orderItem()");
        // 기존의 코드와 동일한 기능을 수행합니다.

    }

}
