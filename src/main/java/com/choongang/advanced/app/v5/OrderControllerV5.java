package com.choongang.advanced.app.v5;

import com.choongang.advanced.trace.logtrace.LogTrace;
import com.choongang.advanced.trace.template.AbstractTemplate;
import com.choongang.advanced.trace.templatecallback.TraceCallback;
import com.choongang.advanced.trace.templatecallback.TraceTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate traceTemplate;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.traceTemplate = new TraceTemplate(trace);
    }

    /**
     *  * 주문 요청
     *  * @param itemId 상품 아이디
     *  * @return 주문 결과
     *  템플릿 콜백 패턴의 동작(call())은 호출 코드(traceTemplate.execute())에 의해 트리거됩니다.
     */
    @GetMapping("/v5/request")
    public String request(String itemId) {
        return traceTemplate.execute("OrderControllerV5.request()", new TraceCallback<String>() {
            @Override
            public String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        });
    }
}
