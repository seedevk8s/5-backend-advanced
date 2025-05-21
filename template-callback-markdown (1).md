# 템플릿 콜백 패턴 (Template Callback Pattern)

## 1. 개요

템플릿 콜백 패턴은 Spring Framework에서 자주 사용되는 디자인 패턴으로, 전략 패턴과 템플릿 메소드 패턴을 결합한 형태입니다. 이 패턴은 반복되는 작업(템플릿)과 변경되는 작업(콜백)을 분리하여 코드 중복을 제거하고 유지보수성을 높입니다. 특히 트랜잭션, 예외 처리, 리소스 해제와 같은 '횡단 관심사(cross-cutting concerns)'를 효과적으로 분리할 수 있게 해줍니다.

## 2. 주요 구성 요소

### 2.1 템플릿 (Template)

- 변하지 않는 공통 로직을 담당하는 클래스
- 콜백 인터페이스를 파라미터로 받아 실행 전/후 로직을 처리
- 예: `TraceTemplate`

### 2.2 콜백 (Callback)

- 변하는 비즈니스 로직을 담당하는 인터페이스
- 템플릿에서 실행될 코드 블록을 정의
- 익명 내부 클래스나 람다로 주로 구현
- 예: `TraceCallback<T>` 인터페이스

## 3. 예제 코드

### 3.1 콜백 인터페이스

```java
package com.choongang.advanced.trace.templatecallback;

/**
 * 템플릿 콜백 패턴에서 사용할 콜백 인터페이스
 * @param <T> 콜백의 반환 타입
 */
public interface TraceCallback<T> {
    /**
     * 콜백으로 실행할 로직
     * @return 콜백 실행 결과
     */
    T call();
}
```

### 3.2 템플릿 클래스

```java
package com.choongang.advanced.trace.templatecallback;

import com.choongang.advanced.trace.TraceStatus;
import com.choongang.advanced.trace.logtrace.LogTrace;

/**
 * 템플릿 콜백 패턴의 템플릿 역할을 하는 클래스
 * 로그 추적 로직의 공통 부분을 처리하고, 변하는 로직은 콜백으로 처리한다.
 */
public class TraceTemplate {
    
    private final LogTrace trace;
    
    public TraceTemplate(LogTrace trace) {
        this.trace = trace;
    }
    
    /**
     * 템플릿 메서드 - 공통 로직 처리
     * @param <T> 콜백의 반환 타입
     * @param message 로그 메시지
     * @param callback 실행할 콜백 (비즈니스 로직)
     * @return 콜백의 실행 결과
     */
    public <T> T execute(String message, TraceCallback<T> callback) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);  // 로그 시작
            
            // 콜백 실행 - 비즈니스 로직
            T result = callback.call();
            
            trace.end(status);  // 로그 정상 종료
            return result;
        } catch (Exception e) {
            trace.exception(status, e);  // 로그 예외 처리
            throw e;  // 예외 재던짐
        }
    }
}
```

### 3.3 적용 예시

```java
// OrderControllerV5
@RestController
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate template;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new TraceTemplate(trace);  // 템플릿 생성
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {
        // 템플릿 콜백 패턴 적용
        return template.execute("OrderController.request()", () -> {
            // 비즈니스 로직 (콜백으로 전달)
            orderService.orderItem(itemId);
            return "ok";
        });
    }
}

// OrderServiceV5
@Service
public class OrderServiceV5 {
    
    private final OrderRepositoryV5 orderRepository;
    private final TraceTemplate template;
    
    public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace trace) {
        this.orderRepository = orderRepository;
        this.template = new TraceTemplate(trace);  // 템플릿 생성
    }
    
    public void orderItem(String itemId) {
        // 템플릿 콜백 패턴 적용
        template.execute("OrderService.orderItem()", () -> {
            // 비즈니스 로직 (콜백으로 전달)
            orderRepository.save(itemId);
            return null;  // 반환값이 없는 경우 null 반환
        });
    }
}

// OrderRepositoryV5
@Repository
public class OrderRepositoryV5 {
    
    private final TraceTemplate template;
    
    public OrderRepositoryV5(LogTrace trace) {
        this.template = new TraceTemplate(trace);  // 템플릿 생성
    }
    
    public void save(String itemId) {
        // 템플릿 콜백 패턴 적용
        template.execute("OrderRepository.save()", () -> {
            // 비즈니스 로직 (콜백으로 전달)
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);  // 상품 저장 지연
            return null;  // 반환값이 없는 경우 null 반환
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

## 4. 템플릿 콜백 패턴의 장점

1. **코드 중복 제거**: 공통 로직(로깅, 트랜잭션 처리, 리소스 해제 등)을 템플릿에 모아 중복을 제거
2. **변경의 유연성**: 비즈니스 로직만 콜백으로 전달하여 변경 용이
3. **익명 내부 클래스/람다**: 간결한 코드 작성 가능
4. **제네릭 활용**: 다양한 반환 타입 지원 가능
5. **SRP 원칙 준수**: 단일 책임 원칙에 따라 템플릿은 공통 로직, 콜백은 비즈니스 로직만 담당
6. **OCP 원칙 준수**: 개방-폐쇄 원칙에 따라 템플릿 코드 변경 없이 콜백 확장 가능
7. **테스트 용이성**: 공통 로직과 비즈니스 로직을 분리하여 테스트 용이

## 5. 실제 활용 사례

Spring Framework에서는 다음과 같은 경우에 템플릿 콜백 패턴을 적극 활용합니다:

- **JdbcTemplate**: SQL 실행과 예외 처리, 커넥션 관리는 템플릿이, 실제 SQL과 파라미터는 콜백이 처리
  ```java
  public List<Member> findAll() {
      return jdbcTemplate.query("select * from members", 
          (ResultSet rs, int rowNum) -> {
              Member member = new Member();
              member.setId(rs.getLong("id"));
              member.setName(rs.getString("name"));
              return member;
          });
  }
  ```

- **TransactionTemplate**: 트랜잭션 시작/종료/롤백은 템플릿이, 비즈니스 로직은 콜백이 처리
  ```java
  public void updateMember(Member member) {
      transactionTemplate.execute(status -> {
          try {
              memberRepository.update(member);
              return true;
          } catch (Exception e) {
              status.setRollbackOnly(); // 롤백 마킹
              throw e;
          }
      });
  }
  ```

- **RestTemplate**: HTTP 통신 처리는 템플릿이, 응답 변환은 콜백이 처리
  ```java
  public User getUser(Long id) {
      return restTemplate.execute(
          "https://api.example.com/users/{id}", 
          HttpMethod.GET,
          null, // RequestCallback
          response -> {
              // ResponseExtractor 콜백
              // 응답 처리 로직
              return objectMapper.readValue(response.getBody(), User.class);
          },
          id
      );
  }
  ```

- **HibernateTemplate**: 세션 관리는 템플릿이, 실제 ORM 작업은 콜백이 처리
- **RedisTemplate**: Redis 커넥션 관리는 템플릿이, 실제 명령은 콜백이 처리

## 6. 관련 디자인 패턴 비교

### 6.1 전략 패턴 vs 템플릿 콜백 패턴

- **전략 패턴**: 객체로 전략을 주입받아 사용
- **템플릿 콜백 패턴**: 메소드 단위로 콜백을 전달하여 사용 (더 유연함)

### 6.2 템플릿 메소드 패턴 vs 템플릿 콜백 패턴

- **템플릿 메소드 패턴**: 상속을 통한 확장 (부모-자식 관계)
- **템플릿 콜백 패턴**: 위임을 통한 확장 (합성 관계)

## 7. 결론

템플릿 콜백 패턴은 변하는 부분과 변하지 않는 부분을 깔끔하게 분리하여 코드의 재사용성과 유지보수성을 크게 향상시키는 패턴입니다. Spring Framework에서 제공하는 다양한 템플릿 클래스들의 핵심 동작 원리가 바로 이 패턴에 기반하고 있으며, 실무에서도 반복되는 패턴의 코드를 효과적으로 처리하는 데 유용하게 활용할 수 있습니다.