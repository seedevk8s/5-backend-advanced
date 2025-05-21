package com.choongang.advanced.trace.strategy.code.templatecallback;

import lombok.extern.slf4j.Slf4j;

/**
 * 템플릿 콜백 패턴을 사용하여 비즈니스 로직을 분리합니다.
 * 템플릿 메서드 패턴과 유사하지만, 템플릿 메서드가 아닌 콜백 메서드를 사용합니다.
 *
 * @see Callback
 */
@Slf4j
public class TimeLogTemplate {

    public void execute(Callback callback) {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        callback.call(); // 콜백 메서드 호출
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        System.out.println("비즈니스 로직 실행 시간: " + resultTime + "ms");
    }
}
