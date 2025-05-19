package com.choongang.advanced.app.v4;

import com.choongang.advanced.trace.TraceStatus;
import com.choongang.advanced.trace.logtrace.LogTrace;
import com.choongang.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {

    private final LogTrace trace;

    public void save(String itemId) {
        // 템플릿 메서드 패턴을 사용하여 비즈니스 로직을 실행합니다.
        AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {
            @Override
            protected Void call() {
                // 저장 로직
                if (itemId.equals("ex")) {
                    throw new IllegalStateException("예외 발생!");
                }
                sleep(1000);
                return null;  // Void 타입이므로 null을 반환합니다.
            }
        };
        template.execute("OrderRepositoryV4.save()");
        // 기존의 코드와 동일한 기능을 수행합니다.
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
