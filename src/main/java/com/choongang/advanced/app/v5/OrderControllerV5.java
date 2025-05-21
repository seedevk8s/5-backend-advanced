package com.choongang.advanced.app.v5;

import com.choongang.advanced.trace.logtrace.LogTrace;
import com.choongang.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final LogTrace trace;       // LogTrace 인터페이스를 사용하여 로그 추적 기능을 구현

    /**
     * 주문 요청
     * @param itemId 상품 아이디
     * @return 결과
     */
    @GetMapping("/v5/request")
    public String request(String itemId) {

        // 템플릿 메서드 패턴을 사용하여 비즈니스 로직을 실행합니다.
        AbstractTemplate<String> template = new AbstractTemplate<>(trace) {
            @Override
            protected String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        };
        return template.execute("OrderControllerV4.request()");
        // 기존의 코드와 동일한 기능을 수행합니다.
    }
}
