package com.choongang.advanced.app.v1;

import com.choongang.advanced.trace.TraceStatus;
import com.choongang.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderServiceV1 orderService;
    private final HelloTraceV1 trace;

    /**
     * 주문 요청
     * @param itemId 상품 아이디
     * @return 결과
     */
    @GetMapping("/v1/request")
    public String request(String itemId) {

        TraceStatus status = null;
        // 비즈니스 로직
        // 주문 요청
        try {
            status = trace.begin("OrderControllerV1.request()");
            orderService.orderItem(itemId);
            trace.end(status);
            return "ok";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;  // 예외를 꼭 던져야 한다.
        }




    }
}
