# TraceTemplate의 명시적 생성자 초기화 패턴

## 1. 개요

템플릿 콜백 패턴을 구현할 때 `OrderControllerV5`, `OrderServiceV5`, `OrderRepositoryV5` 컴포넌트에서 공통적으로 `TraceTemplate`을 명시적인 생성자를 통해 초기화하는 패턴을 적용했습니다. 이 패턴은 컴포넌트 내에서 필요한 템플릿 객체를 직접 생성하여 관리하는 방식으로, Spring의 의존성 주입과 함께 사용되는 초기화 전략입니다.

## 2. 전체 구성

애플리케이션의 전체 구성은 다음과 같습니다:

1. **LogTraceConfig**: `LogTrace` 구현체를 스프링 빈으로 등록하는 설정 클래스
2. **LogTrace**: 로그 추적을 위한 인터페이스
3. **ThreadLocalLogTrace**: `LogTrace`의 구현체, ThreadLocal을 사용하여 쓰레드 안전성 보장
4. **TraceTemplate**: 템플릿 역할을 하는 클래스, 각 컴포넌트에서 직접 생성
5. **TraceCallback**: 콜백 인터페이스
6. **OrderControllerV5, OrderServiceV5, OrderRepositoryV5**: 비즈니스 로직 컴포넌트

## 3. 코드 예시

### 3.1. LogTraceConfig

```java
@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}
```

### 3.2. OrderControllerV5

```java
@RestController
// @RequiredArgsConstructor 사용하지 않음
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate template; // 템플릿 필드

    // 명시적 생성자를 통한 초기화
    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new TraceTemplate(trace); // 템플릿 객체 직접 생성
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {
        return template.execute("OrderController.request()", () -> {
            orderService.orderItem(itemId);
            return "ok";
        });
    }
}
```

### 3.3. OrderServiceV5

```java
@Service
// @RequiredArgsConstructor 사용하지 않음
public class OrderServiceV5 {
    
    private final OrderRepositoryV5 orderRepository;
    private final TraceTemplate template; // 템플릿 필드
    
    // 명시적 생성자를 통한 초기화
    public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace trace) {
        this.orderRepository = orderRepository;
        this.template = new TraceTemplate(trace); // 템플릿 객체 직접 생성
    }
    
    public void orderItem(String itemId) {
        template.execute("OrderService.orderItem()", () -> {
            orderRepository.save(itemId);
            return null;
        });
    }
}
```

### 3.4. OrderRepositoryV5

```java
@Repository
// @RequiredArgsConstructor 사용하지 않음
public class OrderRepositoryV5 {

    private final TraceTemplate template; // 템플릿 필드
    
    // 명시적 생성자를 통한 초기화
    public OrderRepositoryV5(LogTrace trace) {
        this.template = new TraceTemplate(trace); // 템플릿 객체 직접 생성
    }
    
    public void save(String itemId) {
        template.execute("OrderRepository.save()", () -> {
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

## 4. 의존성 흐름

1. Spring Container는 `LogTraceConfig`의 `@Bean` 메서드를 통해 `ThreadLocalLogTrace` 구현체를 생성하고 `LogTrace` 타입의 빈으로 등록합니다.

2. Spring Container는 `OrderRepositoryV5`, `OrderServiceV5`, `OrderControllerV5`를 순차적으로 생성하면서 각 컴포넌트에 필요한 의존성을 주입합니다:
   - `OrderRepositoryV5`에는 `LogTrace` 빈을 주입
   - `OrderServiceV5`에는 `OrderRepositoryV5`와 `LogTrace` 빈을 주입
   - `OrderControllerV5`에는 `OrderServiceV5`와 `LogTrace` 빈을 주입

3. 각 컴포넌트의 생성자 내부에서는 주입받은 `LogTrace`를 사용하여 `TraceTemplate` 인스턴스를 직접 생성하고 필드에 할당합니다:
   ```java
   this.template = new TraceTemplate(trace);
   ```

4. 모든 컴포넌트는 자신만의 `TraceTemplate` 인스턴스를 가지게 되지만, 내부적으로는 동일한 `LogTrace` 구현체를 참조합니다.

## 5. 명시적 생성자 초기화 패턴의 특징

### 5.1. 기본 구조

1. **필드 선언**: 각 컴포넌트에서 `TraceTemplate` 타입의 필드를 선언합니다.
2. **생성자 파라미터**: 생성자에서 `LogTrace`를 파라미터로 받습니다.
3. **객체 생성**: 생성자 내부에서 `new TraceTemplate(trace)`를 통해 템플릿 객체를 직접 생성합니다.
4. **참조 저장**: 생성된 템플릿 객체를 필드에 저장하여 컴포넌트 내에서 사용합니다.

### 5.2. 장점

1. **명확한 의존성**: 컴포넌트가 어떤 객체에 의존하는지 명확하게 보여줍니다.
2. **유연한 초기화**: 템플릿 객체를 생성할 때 추가적인 설정이나 커스터마이징이 가능합니다.
3. **의존성 주입 원칙 유지**: 핵심 의존성인 `LogTrace`는 여전히 외부에서 주입받습니다.
4. **내부 관심사 캡슐화**: `TraceTemplate`은 컴포넌트의 내부 구현 세부사항으로 간주합니다.
5. **개별적인 템플릿 인스턴스**: 각 컴포넌트마다 독립적인 템플릿 인스턴스를 가지므로 필요에 따라 다르게 구성 가능합니다.

### 5.3. 단점

1. **객체 중복 생성**: 동일한 `TraceTemplate` 객체가 여러 컴포넌트에서 중복 생성됩니다.
2. **생성자 코드 중복**: 템플릿 객체를 생성하는 코드가 여러 컴포넌트에 중복됩니다.
3. **직접 초기화**: 직접 객체를 생성하므로 의존성 주입의 일부 이점을 포기합니다.

## 6. 대안적인 접근 방식

### 6.1. 템플릿 빈으로 등록

```java
@Configuration
public class TraceConfig {
    
    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
    
    @Bean
    public TraceTemplate traceTemplate(LogTrace logTrace) {
        return new TraceTemplate(logTrace);
    }
}
```

```java
@RestController
@RequiredArgsConstructor
public class OrderControllerV5Alt {
    private final OrderServiceV5Alt orderService;
    private final TraceTemplate traceTemplate; // 빈으로 주입
    
    // 나머지 코드는 동일
}
```

### 6.2. 템플릿 팩토리 메서드 사용

```java
@Component
public class TraceTemplateFactory {
    private final LogTrace logTrace;
    
    public TraceTemplateFactory(LogTrace logTrace) {
        this.logTrace = logTrace;
    }
    
    public TraceTemplate create() {
        return new TraceTemplate(logTrace);
    }
}
```

## 7. 명시적 생성자 초기화를 사용하는 이유

1. **단순성**: 추가적인 설정 클래스나 팩토리 없이 간단한 구현이 가능합니다.
2. **컴포넌트 자율성**: 각 컴포넌트가 자신의 템플릿 객체를 직접 관리합니다.
3. **명확한 의존성 흐름**: `LogTrace`만 외부에서 주입받고, `TraceTemplate`은 내부 구현 세부사항임을 명확히 합니다.
4. **실용적 접근**: 실제 애플리케이션 개발에서 합리적인 균형점을 제공합니다.

## 8. 결론

명시적인 생성자를 통한 `TraceTemplate` 초기화 패턴은 의존성 주입 원칙과 실용적인 객체 생성 방식을 절충한 접근법입니다. 이 패턴은 Spring의 의존성 주입 메커니즘을 활용하면서도, 컴포넌트 내부에서 필요한 객체를 직접 생성하고 관리하는 유연성을 제공합니다.

특히 `LogTrace`와 같은 핵심 컴포넌트는 Spring 컨테이너가 관리하고, `TraceTemplate`과 같은 보조 객체는 필요한 컴포넌트에서 직접 생성하는 방식으로 관심사의 분리와 간결한 구현 사이의 균형을 맞춘 설계입니다.

템플릿 콜백 패턴과 함께 사용할 경우, 핵심 로직에 집중하면서도 공통 관심사(로깅, 트랜잭션 등)를 효과적으로 분리할 수 있는 강력한 구현 전략이 됩니다.