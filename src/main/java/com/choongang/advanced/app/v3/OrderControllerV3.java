package com.choongang.advanced.app.v3;

import com.choongang.advanced.trace.TraceStatus;

import com.choongang.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {

    private final OrderServiceV3 orderService;
    private final LogTrace trace;       // LogTrace 인터페이스를 사용하여 로그 추적 기능을 구현

    /**
     * 주문 요청
     * @param itemId 상품 아이디
     * @return 결과
     */
    @GetMapping("/v3/request")
    public String request(String itemId) {

        TraceStatus status = null;
        // 비즈니스 로직
        // 주문 요청
        try {
            status = trace.begin("OrderControllerV3.request()");
            orderService.orderItem(itemId);
            trace.end(status);
            return "ok";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;  // 예외를 꼭 던져야 한다.
        }




    }
}
