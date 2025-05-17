package com.choongang.advanced.app.v1;

import com.choongang.advanced.trace.TraceStatus;
import com.choongang.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {

    private final OrderRepositoryV1 orderRepository;
    private final HelloTraceV1 trace;

    public void orderItem(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderServiceV1.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);
        } catch (Exception e) {
           trace.exception(status, e);
           throw e;  // 예외를 꼭 던져야 한다.
        }

    }

}
