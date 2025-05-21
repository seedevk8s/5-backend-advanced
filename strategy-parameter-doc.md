# 파라미터로 전략을 전달하는 전략 패턴 (ContextV2)

## 1. 개요

이 문서는 전략 패턴(Strategy Pattern)의 또 다른 구현 방식인 '파라미터로 전략을 전달하는 방식'에 대해 설명합니다. 이 방식에서는 컨텍스트가 전략을 필드로 가지고 있지 않고, 실행 시점에 전략을 파라미터로 전달받아 사용합니다.

## 2. 파라미터 전략 패턴의 구조

### 2.1 핵심 구성 요소

1. **Strategy (전략)**: 알고리즘을 정의하는 인터페이스
2. **ConcreteStrategy (구체 전략)**: Strategy 인터페이스를 구현한 클래스
3. **Context (컨텍스트)**: 전략을 실행하는 클래스, 전략을 파라미터로 전달받음

### 2.2 주요 코드 구조

```java
// 전략 인터페이스
public interface Strategy {
    void call();
}

// 구체적인 전략 구현체
public class StrategyLogic1 implements Strategy {
    @Override
    public void call() {
        log.info("비즈니스 로직1 실행");
    }
}

public class StrategyLogic2 implements Strategy {
    @Override
    public void call() {
        log.info("비즈니스 로직2 실행");
    }
}

// 컨텍스트 - 전략을 파라미터로 전달받음
public class ContextV2 {
    public void execute(Strategy strategy) {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행 전 공통 작업
        log.info("비즈니스 로직 실행 시작");
        
        // 위임 - 파라미터로 전달받은 전략 실행
        strategy.call();
        
        // 비즈니스 로직 실행 후 공통 작업
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("비즈니스 로직 실행 종료, resultTime={}", resultTime);
    }
}
```

### 2.3 테스트 코드

```java
/**
 * 전략을 파라미터로 전달하는 방식
 */
@Test
void contextV2Test() {
    // 컨텍스트 생성
    ContextV2 context = new ContextV2();
    
    // 전략1 생성 및 실행
    context.execute(new StrategyLogic1());
    
    // 전략2 생성 및 실행
    context.execute(new StrategyLogic2());
}
```

## 3. 익명 내부 클래스를 활용한 파라미터 전략 패턴

전략을 파라미터로 전달하는 방식은 익명 내부 클래스와 함께 사용하면 더욱 간결하게 구현할 수 있습니다.

```java
/**
 * 전략을 파라미터로 전달하는 방식 - 익명 내부 클래스 사용
 */
@Test
void contextV2Test2() {
    // 컨텍스트 생성
    ContextV2 context = new ContextV2();
    
    // 익명 내부 클래스로 전략1 생성 및 실행
    context.execute(new Strategy() {
        @Override
        public void call() {
            log.info("비즈니스 로직1 실행");
        }
    });
    
    // 익명 내부 클래스로 전략2 생성 및 실행
    context.execute(new Strategy() {
        @Override
        public void call() {
            log.info("비즈니스 로직2 실행");
        }
    });
}
```

## 4. 람다 표현식을 활용한 파라미터 전략 패턴

Java 8 이상에서는 전략 인터페이스가 함수형 인터페이스(추상 메서드가 하나만 있는 인터페이스)인 경우, 람다 표현식을 사용하여 더욱 간결하게 구현할 수 있습니다.

```java
/**
 * 전략을 파라미터로 전달하는 방식 - 람다 표현식 사용
 */
@Test
void contextV2Test3() {
    // 컨텍스트 생성
    ContextV2 context = new ContextV2();
    
    // 람다 표현식으로 전략1 생성 및 실행
    context.execute(() -> log.info("비즈니스 로직1 실행"));
    
    // 람다 표현식으로 전략2 생성 및 실행
    context.execute(() -> log.info("비즈니스 로직2 실행"));
}
```

## 5. 필드 방식(ContextV1)과 파라미터 방식(ContextV2) 비교

### 5.1 전략 보관 방식

- **ContextV1 (필드)**: 
  - 필드에 전략을 보관
  - 컨텍스트 생성 시점에 전략을 주입받음
  - 컨텍스트 생성 후에는 전략을 변경하기 어려움 (setter 필요)

- **ContextV2 (파라미터)**: 
  - 필드에 전략을 보관하지 않음
  - 실행할 때마다 전략을 파라미터로 전달받음
  - 실행할 때마다 다른 전략을 유연하게 전달 가능

### 5.2 전략 변경 시점

- **ContextV1 (필드)**: 
  - 컨텍스트 생성 시점에 전략이 결정됨
  - 전략을 변경하려면 새로운 컨텍스트를 생성하거나 setter 필요

- **ContextV2 (파라미터)**: 
  - 컨텍스트 실행 시점에 전략이 결정됨
  - 메서드 호출마다 다른 전략을 자유롭게 전달 가능

### 5.3 인스턴스 생성 횟수

- **ContextV1 (필드)**: 
  - 전략이 바뀔 때마다 새로운 컨텍스트 인스턴스 생성 필요
  - 전략별로 컨텍스트 객체가 생성됨

- **ContextV2 (파라미터)**: 
  - 컨텍스트 인스턴스를 한 번만 생성하고 재사용
  - 여러 전략을 동일한 컨텍스트에서 실행 가능

## 6. 파라미터 전략 패턴의 장단점

### 6.1 장점

1. **유연성**: 실행 시점에 전략을 자유롭게 변경 가능
2. **재사용성**: 동일한 컨텍스트 인스턴스를 여러 전략에 재사용 가능
3. **간결성**: 컨텍스트 생성 코드가 간결해짐
4. **일관성**: 전략 호출 방식이 일관적임

### 6.2 단점

1. **실행마다 전략 전달**: 매번 실행할 때마다 전략을 전달해야 함
2. **전략 공유 어려움**: 여러 메서드에서 동일한 전략을 공유하기 어려움
3. **선택 책임**: 클라이언트가 적절한 전략을 선택해야 함

## 7. 실제 활용 예시

### 7.1 Java의 Collections.sort()

Java의 Collections.sort() 메서드는 파라미터로 전략을 전달하는 방식의 대표적인 예입니다.

```java
List<Person> people = new ArrayList<>();
// ... people 추가

// Comparator를 파라미터로 전달
Collections.sort(people, new Comparator<Person>() {
    @Override
    public int compare(Person o1, Person o2) {
        return o1.getAge() - o2.getAge(); // 나이로 정렬
    }
});

// 람다 표현식 사용
Collections.sort(people, (p1, p2) -> p1.getName().compareTo(p2.getName())); // 이름으로 정렬
```

### 7.2 ExecutorService의 execute()

ExecutorService의 execute() 메서드도 파라미터로 전략(Runnable)을 전달받습니다.

```java
ExecutorService executorService = Executors.newFixedThreadPool(10);

// Runnable을 파라미터로 전달
executorService.execute(new Runnable() {
    @Override
    public void run() {
        System.out.println("작업 실행");
    }
});

// 람다 표현식 사용
executorService.execute(() -> System.out.println("작업 실행"));
```

## 8. 결론

파라미터로 전략을 전달하는 방식(ContextV2)은 실행 시점에 유연하게 전략을 변경할 수 있다는 큰 장점이 있습니다. 특히 한 번만 생성된 컨텍스트를 재사용하면서 다양한 전략을 실행할 수 있기 때문에, 전략이 자주 변경되는 상황에서 유용합니다.

필드로 전략을 보관하는 방식(ContextV1)과 파라미터로 전략을 전달하는 방식(ContextV2)은 각각 장단점이 있으므로, 상황에 맞게 적절한 방식을 선택하는 것이 중요합니다. 두 방식을 조합하여 사용하는 것도 가능합니다.

전략 패턴의 두 가지 구현 방식(필드와 파라미터)은 모두 공통적으로 변하지 않는 부분(컨텍스트)과 변하는 부분(전략)을 분리하고, 변하는 부분을 캡슐화한다는 객체 지향 설계의 핵심 원칙을 따르고 있습니다.
