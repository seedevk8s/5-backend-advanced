package com.choongang.advanced.app.v3;

import com.choongang.advanced.trace.TraceId;
import com.choongang.advanced.trace.TraceStatus;
import com.choongang.advanced.trace.hellotrace.HelloTraceV2;
import com.choongang.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {

    private final OrderRepositoryV3 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderServiceV3.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);
        } catch (Exception e) {
           trace.exception(status, e);
           throw e;  // 예외를 꼭 던져야 한다.
        }

    }

}
