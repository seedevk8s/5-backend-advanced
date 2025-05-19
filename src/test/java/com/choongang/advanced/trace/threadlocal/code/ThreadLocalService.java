package com.choongang.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalService {

    private ThreadLocal<String> nameStore = new ThreadLocal<>();

    public String logic(String name) {
        log.info("저장 name = {} -> nameStore={}", name, nameStore.get());
        nameStore.set(name); // name을 ThreadLocal에 저장
        sleep(1000);
        log.info("조회 nameStore={}", nameStore.get());
        return nameStore.get();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 별도의 정리 메서드 제공
    public void clearNameStore() {
        nameStore.remove();
        log.info("ThreadLocal 값 제거 완료");
    }
}
