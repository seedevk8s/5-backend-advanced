package com.choongang.advanced.app.v5;

import com.choongang.advanced.trace.logtrace.LogTrace;
import com.choongang.advanced.trace.template.AbstractTemplate;
import com.choongang.advanced.trace.templatecallback.TraceCallback;
import com.choongang.advanced.trace.templatecallback.TraceTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceV5 {

    private final OrderRepositoryV5 orderRepository;
    private final TraceTemplate traceTemplate;      // 템플릿 콜백을 사용하기 위한 템플릿 객체

    public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace trace) {
        this.orderRepository = orderRepository;
        this.traceTemplate = new TraceTemplate(trace); // 템플릿 객체 생성
    }

    /**
     * 주문 아이템
     * @param itemId 상품 아이디
     * 템플릿 콜백 패턴의 동작(call())은 호출 코드(traceTemplate.execute())에 의해 트리거됩니다.
     */
    public void orderItem(String itemId) {
        // 템플릿 콜백을 사용하여 비즈니스 로직을 실행합니다.
        // 템플릿을 실행하면서 콜백을 전달한다.
        traceTemplate.execute("OrderServiceV5.orderItem()", new TraceCallback<Void>() {
            @Override
            public Void call() {
                orderRepository.save(itemId);
                return null;
            }
        });

    }

}
