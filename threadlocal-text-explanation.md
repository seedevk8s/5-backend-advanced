# ThreadLocal 데이터 저장 구조

ThreadLocal은 각 스레드가 독립적인 변수 값을 가질 수 있도록 해주는 Java의 특별한 변수 유형입니다.

## 기본 구조

```
ThreadLocal<TraceId> traceIdHolder
   |
   |--- Thread 1 --> TraceId(id="tx1", level=0)
   |
   |--- Thread 2 --> TraceId(id="tx2", level=0)
```

## 핵심 특징

1. **하나의 ThreadLocal 객체**가 여러 스레드에서 공유됨
2. 각 **스레드는 자신만의 독립적인 값**을 가짐
3. 한 스레드의 값 변경이 다른 스레드에 영향을 주지 않음
4. 각 스레드는 ThreadLocal.get() 호출 시 자신의 값만 받음

## 내부 동작 원리

1. 각 스레드는 ThreadLocalMap이라는 Map 구조를 가짐
2. ThreadLocal 객체를 키로 사용해 값을 저장/조회
3. 따라서 동일한 ThreadLocal을 사용해도 다른 스레드와 간섭이 없음

## ThreadLocalLogTrace에서의 활용

- 로그 추적 시 각 요청을 처리하는 스레드별로 독립적인 TraceId 관리
- 각 HTTP 요청이 별도 스레드에서 처리되어도 로그 정보가 섞이지 않음
- 요청 처리 완료 시 ThreadLocal.remove()로 메모리 정리
