package com.choongang.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrategyLogic1 implements Strategy {
    @Override
    public void call() {
        log.info("StrategyLogic1 call-비즈니스 로직1 실행");
    }
}
