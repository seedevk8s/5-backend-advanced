package com.choongang.advanced.app.v5;

import com.choongang.advanced.trace.logtrace.LogTrace;
import com.choongang.advanced.trace.template.AbstractTemplate;
import com.choongang.advanced.trace.templatecallback.TraceCallback;
import com.choongang.advanced.trace.templatecallback.TraceTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
public class OrderRepositoryV5 {

    private final TraceTemplate traceTemplate;      // 템플릿 콜백을 사용하기 위한 템플릿 객체

    // 템플릿 콜백을 사용하기 위한 템플릿 객체를 생성합니다.
    public OrderRepositoryV5(LogTrace trace) {
        this.traceTemplate = new TraceTemplate(trace); // 템플릿 객체 생성
    }

    /**
     * 주문 아이템 저장
     * @param itemId 상품 아이디
     * 템플릿 콜백 패턴의 동작(call())은 호출 코드(traceTemplate.execute())에 의해 트리거됩니다.
     */
    public void save(String itemId) {

        traceTemplate.execute("OrderRepositoryV5.save()", new TraceCallback<Void>() {
            @Override
            public Void call() {
                // 저장 로직
                if (itemId.equals("ex")) {
                    throw new IllegalStateException("예외 발생!");
                }
                sleep(1000); // 1초 대기
                return null;
            }
        });

    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
