package com.choongang.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * ContextV1는 전략을 실행하는 클래스입니다.
 * 전략을 주입받아 비즈니스 로직을 실행합니다.
 * 이 클래스는 전략을 변경할 수 있는 유연성을 제공합니다.
 */
@Slf4j
public class ContextV1 {
    private final Strategy strategy;

    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        strategy.call(); // 위임
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("Strategy execution time: " + resultTime + "ms");
    }
}
