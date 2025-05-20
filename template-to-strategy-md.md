# 템플릿 메서드 패턴에서 전략 패턴으로 V4 코드 리팩토링

## 1. 전략 패턴 개요

GOF 디자인 패턴에서 정의한 전략 패턴(Strategy Pattern)은 알고리즘 군을 정의하고, 각각을 캡슐화하여 교환 가능하게 만드는 패턴입니다. 이를 통해 알고리즘을 사용하는 클라이언트와 독립적으로 알고리즘을 변경할 수 있습니다.

### 템플릿 메서드 패턴과 전략 패턴의 차이

| 항목 | 템플릿 메서드 패턴 | 전략 패턴 |
|------|-------------------|----------|
| 구현 방식 | 상속 | 위임 |
| 확장 메커니즘 | 서브클래스 | 객체 조합 |
| 알고리즘 변형 | 훅 메서드 오버라이딩 | 다른 전략 객체 교체 |
| 결합도 | 상대적으로 높음 | 낮음 |
| 런타임 교체 | 어려움 | 용이함 |

## 2. V4 코드의 문제점과 전략 패턴 적용 동기

현재 V4 코드는 템플릿 메서드 패턴을 사용하여 로그 추적 로직과 비즈니스 로직을 분리했습니다. 그러나 다음과 같은 한계가 있습니다:

1. **상속 기반 설계**: 상속은 강한 결합도를 만들고 유연성을 제한합니다.
2. **익명 내부 클래스 사용**: 코드 가독성과 재사용성이 떨어집니다.
3. **런타임 전략 변경 어려움**: 런타임에 로깅 전략을 변경하기 어렵습니다.

전략 패턴은 이러한 문제를 해결하면서도 핵심 관심사와 부가 관심사의 분리라는 원래 목표를 유지할 수 있습니다.

## 3. 전략 패턴 구현 계획

### 3.1 핵심 구성 요소

1. **Context (TraceContext)**
   - 로그 추적 흐름을 제어하는 컨텍스트 클래스
   - LogTrace를 가지고 있음
   - 다양한 전략 객체를 수용

2. **Strategy (TraceStrategy)**
   - 비즈니스 로직을 캡슐화하는 인터페이스
   - 템플릿 메서드 패턴의 call() 메서드와 유사한 역할

3. **Concrete Strategies**
   - ControllerStrategy, ServiceStrategy, RepositoryStrategy 등
   - 각 계층의 비즈니스 로직 구현

### 3.2 주요 코드 구조

```java
// 전략 인터페이스
public interface TraceStrategy<T> {
    T call();
}

// 컨텍스트 클래스
public class TraceContext {
    private final LogTrace trace;
    
    public TraceContext(LogTrace trace) {
        this.trace = trace;
    }
    
    public <T> T execute(TraceStrategy<T> strategy, String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);
            T result = strategy.call();
            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}

// 컨트롤러용 구체적 전략
public class ControllerStrategy implements TraceStrategy<String> {
    private final OrderService orderService;
    private final String itemId;
    
    public ControllerStrategy(OrderService orderService, String itemId) {
        this.orderService = orderService;
        this.itemId = itemId;
    }
    
    @Override
    public String call() {
        orderService.orderItem(itemId);
        return "ok";
    }
}
```

## 4. 개선된 V4 코드

### 4.1 OrderController

```java
@RestController
@RequiredArgsConstructor
public class OrderControllerV4 {

    private final OrderServiceV4 orderService;
    private final TraceContext traceContext;

    @GetMapping("/v4/request")
    public String request(String itemId) {
        // 전략 패턴 적용
        TraceStrategy<String> strategy = new ControllerStrategy(orderService, itemId);
        return traceContext.execute(strategy, "OrderController.request()");
    }
}
```

### 4.2 OrderService

```java
@Service
@RequiredArgsConstructor
public class OrderServiceV4 {

    private final OrderRepositoryV4 orderRepository;
    private final TraceContext traceContext;

    public void orderItem(String itemId) {
        // 전략 패턴 적용
        TraceStrategy<Void> strategy = new ServiceStrategy(orderRepository, itemId);
        traceContext.execute(strategy, "OrderService.orderItem()");
    }
}
```

### 4.3 OrderRepository

```java
@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {

    private final TraceContext traceContext;

    public void save(String itemId) {
        // 전략 패턴 적용
        TraceStrategy<Void> strategy = new RepositoryStrategy(itemId);
        traceContext.execute(strategy, "OrderRepository.save()");
    }
    
    private static class RepositoryStrategy implements TraceStrategy<Void> {
        private final String itemId;
        
        public RepositoryStrategy(String itemId) {
            this.itemId = itemId;
        }
        
        @Override
        public Void call() {
            // 저장 로직
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);
            return null;
        }
        
        private void sleep(int millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

### 4.4 Config

```java
@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        return new FieldLogTrace();
    }
    
    @Bean
    public TraceContext traceContext(LogTrace logTrace) {
        return new TraceContext(logTrace);
    }
}
```

## 5. 전략 패턴 적용의 이점

1. **더 높은 유연성**
   - 동일한 컨텍스트에서 다양한 전략을 사용 가능
   - 런타임에 전략 교체 용이

2. **더 나은 재사용성**
   - 전략 클래스들을 독립적으로 재사용 가능
   - 상속 없이 조합을 통한 재사용

3. **더 낮은 결합도**
   - Context와 Strategy 간의 인터페이스 기반 결합
   - 구체적인 구현체에 의존하지 않음

4. **테스트 용이성 향상**
   - 각 전략을 독립적으로 테스트 가능
   - 모의 객체(Mock)를 활용한 테스트 단순화

## 6. 후속 발전 가능성

1. **전략 팩토리 도입**
   - 전략 객체 생성 로직을 팩토리로 분리
   - 클라이언트 코드 단순화

2. **전략 레지스트리**
   - 자주 사용하는 전략을 등록하고 재사용
   - 메모리 및 성능 최적화

3. **컴포지트 전략**
   - 여러 전략을 조합한 복합 전략 구현
   - 더 복잡한 비즈니스 로직 지원

4. **애노테이션 기반 확장**
   - 선언적 방식으로 전략 적용
   - AOP와 결합하여 더 간결한 코드 구성

## 7. 결론

템플릿 메서드 패턴에서 전략 패턴으로의 전환은 코드의 유연성과 재사용성을 크게 향상시킵니다. 이는 객체 지향 프로그래밍의 핵심 원칙인 "상속보다는 조합"을 실천하는 좋은 예입니다. 전략 패턴을 통해 로그 추적이라는 부가 기능과 비즈니스 로직이라는 핵심 기능을 더 깔끔하게 분리하고, 런타임에 유연하게 전략을 교체할 수 있게 되었습니다.
