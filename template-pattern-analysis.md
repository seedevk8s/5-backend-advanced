# 템플릿 메서드 패턴을 적용한 로그 추적기 V4 분석

## 커밋 요약
이 커밋(ad5e964f72549b94a0d9c55349a96b9b4a648796)은 V3에서 사용하던 로그 추적 코드를 템플릿 메서드 패턴을 적용하여 V4로 리팩토링한 변경사항입니다. 총 3개 파일이 변경되었으며, 36줄이 추가되고 41줄이 제거되었습니다.

## 변경된 파일
1. OrderControllerV4.java
2. OrderServiceV4.java
3. OrderRepositoryV4.java

## 주요 변경사항

### 1. 템플릿 메서드 패턴 도입
기존 V3 버전에서는 각 계층(컨트롤러, 서비스, 리포지토리)에서 반복적으로 로그 추적 코드(try-catch 블록, trace.begin/end/exception 호출)를 작성했습니다. V4에서는 이 반복 코드를 `AbstractTemplate` 클래스로 추출하여 템플릿 메서드 패턴을 적용했습니다.

### 2. 코드 중복 제거
기존의 중복 코드:
```java
TraceStatus status = null;
try {
    status = trace.begin("...메서드명...");
    // 비즈니스 로직 실행
    trace.end(status);
    return 결과값;
} catch (Exception e) {
    trace.exception(status, e);
    throw e;
}
```

이 중복 코드를 제거하고 각 계층에서는 비즈니스 로직만 `call()` 메서드에 구현하도록 변경했습니다.

### 3. 익명 내부 클래스 사용
각 계층에서는 제네릭을 활용한 `AbstractTemplate`의 익명 내부 클래스를 생성하여 비즈니스 로직을 구현합니다.

```java
AbstractTemplate<String> template = new AbstractTemplate<>(trace) {
    @Override
    protected String call() {
        // 비즈니스 로직만 구현
        return "결과값";
    }
};
return template.execute("메서드명");
```

### 4. 제네릭 타입 활용
- 컨트롤러: `AbstractTemplate<String>` - String 반환 타입
- 서비스/리포지토리: `AbstractTemplate<Void>` - 반환 값 없음 (null 반환)

## 장점

1. **핵심 관심사와 부가 관심사 분리**
   - 핵심 관심사(비즈니스 로직): 개발자가 `call()` 메서드에 직접 구현
   - 부가 관심사(로그 추적): `AbstractTemplate` 클래스에서 처리

2. **코드 중복 제거**
   - 로그 추적 관련 코드(try-catch, trace 메서드 호출)의 중복을 제거

3. **유지보수 향상**
   - 로그 추적 로직이 변경되어도 `AbstractTemplate` 클래스만 수정하면 됨
   - 비즈니스 로직에 집중할 수 있는 구조

## AbstractTemplate 클래스 (예상)

```java
public abstract class AbstractTemplate<T> {
    private final LogTrace trace;
    
    public AbstractTemplate(LogTrace trace) {
        this.trace = trace;
    }
    
    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);
            // 비즈니스 로직 호출 (하위 클래스에서 구현)
            T result = call();
            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
    
    // 하위 클래스에서 구현해야 할 추상 메서드
    protected abstract T call();
}
```

## 정리

이 커밋은 템플릿 메서드 패턴을 적용하여 로그 추적 코드의 중복을 제거하고, 비즈니스 로직과 로그 추적 로직을 명확히 분리했습니다. 이를 통해 코드의 가독성과 유지보수성이 향상되었습니다. 또한 제네릭을 활용하여 다양한 반환 타입에 대응할 수 있는 유연한 구조를 갖추게 되었습니다.
