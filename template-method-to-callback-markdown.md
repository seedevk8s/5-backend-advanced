# 템플릿 메서드 패턴에서 템플릿 콜백 패턴으로의 전환

## 1. 개요

이 문서는 템플릿 메서드 패턴에서 템플릿 콜백 패턴으로 전환하는 과정을 설명합니다. 두 패턴 모두 공통 로직과 변경되는 로직을 분리하는 목적을 가지고 있지만, 구현 방식에서 차이가 있습니다.

## 2. 두 패턴의 비교

| 항목 | 템플릿 메서드 패턴 | 템플릿 콜백 패턴 |
|------|-------------------|-----------------|
| **구현 방식** | 상속(Inheritance) | 위임(Delegation) |
| **확장성** | 클래스 계층 구조 필요 | 인터페이스만 구현하면 됨 |
| **유연성** | 상대적으로 낮음 | 상대적으로 높음 |
| **사용 편의성** | 클래스 생성 필요 | 익명 클래스/람다로 간편하게 사용 |
| **전형적인 예** | AbstractList | JdbcTemplate |

## 3. 코드 변경 예시

### 3.1. 템플릿 메서드 패턴 (OrderRepositoryV4)

```java
@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {
    
    private final LogTrace trace;
    
    public void save(String itemId) {
        // 템플릿 메서드 패턴을 사용하여 비즈니스 로직을 실행합니다.
        AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {
            @Override
            protected Void call() {
                // 저장 로직
                if (itemId.equals("ex")) {
                    throw new IllegalStateException("예외 발생!");
                }
                sleep(1000);
                return null; // Void 타입이므로 null을 반환합니다.
            }
        };
        template.execute("OrderRepositoryV4.save()");
        // 기존의 코드와 동일한 기능을 수행합니다.
    }
    
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

### 3.2. 템플릿 콜백 패턴 (OrderRepositoryV5)

```java
@Repository
public class OrderRepositoryV5 {

    private final TraceTemplate traceTemplate; // 템플릿 콜백을 사용하기 위한 템플릿 객체
    
    // 템플릿 콜백을 사용하기 위한 템플릿 객체를 생성합니다.
    public OrderRepositoryV5(LogTrace trace) {
        this.traceTemplate = new TraceTemplate(trace); // 템플릿 객체 생성
    }
    
    public void save(String itemId) {
        
        traceTemplate.execute("OrderRepositoryV5.save()", new TraceCallback<Void>() {
            @Override
            public Void call() {
                // 저장 로직
                if (itemId.equals("ex")) {
                    throw new IllegalStateException("예외 발생!");
                }
                sleep(1000); // 1초 대기
                return null;
            }
        });
        
    }
    
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

### 3.3. 람다를 사용한 더 간결한 템플릿 콜백 패턴

```java
@Repository
public class OrderRepositoryV5Lambda {

    private final TraceTemplate traceTemplate;
    
    public OrderRepositoryV5Lambda(LogTrace trace) {
        this.traceTemplate = new TraceTemplate(trace);
    }
    
    public void save(String itemId) {
        traceTemplate.execute("OrderRepositoryV5.save()", () -> {
            // 저장 로직
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);
            return null;
        });
    }
    
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

## 4. 주요 변경 사항

1. **의존성 변경**:
   - 템플릿 메서드 패턴: `LogTrace`에 직접 의존
   - 템플릿 콜백 패턴: `TraceTemplate`에 의존하고, 이 템플릿이 `LogTrace`를 사용

2. **코드 구조 변경**:
   - 템플릿 메서드 패턴: `AbstractTemplate` 클래스를 상속받는 익명 클래스 생성 후 사용
   - 템플릿 콜백 패턴: `TraceCallback` 인터페이스를 구현하는 객체를 전달

3. **로직 분리 방식**:
   - 템플릿 메서드 패턴: 상속을 통해 `call()` 메서드 오버라이드
   - 템플릿 콜백 패턴: 인터페이스 구현체를 파라미터로 전달

4. **생성자 주입 방식**:
   - 템플릿 메서드 패턴: `@RequiredArgsConstructor`를 사용한 자동 주입
   - 템플릿 콜백 패턴: 명시적인 생성자를 통해 `TraceTemplate` 생성 및 초기화

## 5. 장단점 분석

### 5.1. 템플릿 메서드 패턴

#### 장점:
- 구조가 직관적이고 이해하기 쉬움
- 상속 관계가 명확하게 드러남

#### 단점:
- 상속을 사용하므로 다중 상속이 불가능한 Java에서 제약이 있음
- 로직마다 별도의 클래스(익명 클래스)를 생성해야 함
- 상속 구조로 인한 결합도 증가

### 5.2. 템플릿 콜백 패턴

#### 장점:
- 인터페이스 기반으로 더 유연한 확장 가능
- 람다 표현식을 활용하면 더욱 간결한 코드 작성 가능
- 메서드 단위로 전략을 변경할 수 있어 더 세밀한 제어 가능

#### 단점:
- 구조가 상대적으로 복잡할 수 있음
- 콜백 객체 생성 비용(Java 8 이전)
- 콜백 지옥(Callback Hell)에 빠질 위험성

## 6. 결론

템플릿 메서드 패턴에서 템플릿 콜백 패턴으로의 전환은 코드의 유연성과 재사용성을 높이는 방향으로 발전하는 과정입니다. 특히 Java 8 이후 람다 표현식을 활용하면 템플릿 콜백 패턴의 단점을 많이 극복할 수 있게 되었습니다.

Spring Framework에서는 템플릿 콜백 패턴을 적극적으로 활용하고 있으며(JdbcTemplate, TransactionTemplate 등), 이는 객체지향 설계 원칙 중 하나인 "상속보다는 컴포지션을 사용하라"는 원칙에도 부합합니다.