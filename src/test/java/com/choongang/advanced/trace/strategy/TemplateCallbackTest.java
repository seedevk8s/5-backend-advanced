package com.choongang.advanced.trace.strategy;

import com.choongang.advanced.trace.strategy.code.templatecallback.Callback;
import com.choongang.advanced.trace.strategy.code.templatecallback.TimeLogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 템플릿 콜백 패턴을 사용하여 비즈니스 로직을 분리합니다.
 * 템플릿 메서드 패턴과 유사하지만, 템플릿 메서드가 아닌 콜백 메서드를 사용합니다.
 *
 * @see Callback
 */
@Slf4j
public class TemplateCallbackTest {

    /**
     * 템플릿 콜백 패턴을 사용하여 비즈니스 로직을 분리합니다.
     * 익명 내부 클래스를 사용하여 전략을 구현합니다.
     */
    @Test
    void templateCallbackV1() {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
    }

    /**
     * 템플릿 콜백 패턴을 사용하여 비즈니스 로직을 분리합니다.
     * 람다식을 사용하여 전략을 구현합니다.
     */
    @Test
    void templateCallbackV2() {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(() -> log.info("비즈니스 로직1 실행"));
        template.execute(() -> log.info("비즈니스 로직2 실행"));
    }
}
