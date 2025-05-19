package com.choongang.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate {

    public void execute() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        call(); // 하위 클래스에서 구현한 메서드 호출
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("비즈니스 로직 종료, 시간={}", resultTime);
    }

    protected abstract void call(); // 하위 클래스에서 구현해야 하는 메서드


}
