package com.choongang.advanced.trace.templatecallback;

public interface TraceCallback<T> {
    T call(); // 비즈니스 로직을 수행하는 메서드
}
