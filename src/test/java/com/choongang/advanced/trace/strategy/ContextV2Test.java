package com.choongang.advanced.trace.strategy;

import com.choongang.advanced.trace.strategy.code.strategy.ContextV2;
import com.choongang.advanced.trace.strategy.code.strategy.Strategy;
import com.choongang.advanced.trace.strategy.code.strategy.StrategyLogic1;
import com.choongang.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV2Test {

    /**
     * 전략 패턴을 사용하여 비즈니스 로직을 분리합니다.
     * 익명 내부 클래스를 사용하여 전략을 구현합니다.
     */

    @Test
    void strategyV1() {
        ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }

    /**
     * 전략 패턴을 사용하여 비즈니스 로직을 분리합니다.
     * 익명 내부 클래스를 사용하여 전략을 구현합니다.
     */
    @Test
    void strategyV2() {
        ContextV2 context = new ContextV2();
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
    }

    /**
     * 전략 패턴을 사용하여 비즈니스 로직을 분리합니다.
     * 람다를 사용하여 전략을 구현합니다.
     */
    @Test
    void strategyV3() {
        ContextV2 context = new ContextV2();
        context.execute(() -> log.info("비즈니스 로직1 실행"));
        context.execute(() -> log.info("비즈니스 로직2 실행"));
    }
}
