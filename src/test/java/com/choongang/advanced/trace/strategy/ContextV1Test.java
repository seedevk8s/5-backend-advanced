package com.choongang.advanced.trace.strategy;

import com.choongang.advanced.trace.strategy.code.strategy.ContextV1;
import com.choongang.advanced.trace.strategy.code.strategy.StrategyLogic1;
import com.choongang.advanced.trace.strategy.code.strategy.StrategyLogic2;
import com.choongang.advanced.trace.template.code.AbstractTemplate;
import com.choongang.advanced.trace.template.code.SubClassLogic1;
import com.choongang.advanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0() {
        // Client Code
        logic1();
        logic2();
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("비즈니스 로직2 종료, 시간={}", resultTime);
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("비즈니스 로직1 종료, 시간={}", resultTime);
    }

    /**
     * 전략 패턴을 사용하여 비즈니스 로직을 분리합니다.
     */
    @Test
    void strategyV1() {
        // Client Code
        StrategyLogic1 strategyLogic1 = new StrategyLogic1();
        ContextV1 context1 = new ContextV1(strategyLogic1); // 전략을 주입합니다.
        context1.execute();

        StrategyLogic2 strategyLogic2 = new StrategyLogic2();
        ContextV1 context2 = new ContextV1(strategyLogic2); // 전략을 주입합니다.
        context2.execute();
    }
}


