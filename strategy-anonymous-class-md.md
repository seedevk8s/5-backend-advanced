# 전략 패턴과 익명 내부 클래스

## 1. 개요

이 문서는 전략 패턴(Strategy Pattern)에서 익명 내부 클래스(Anonymous Inner Class)를 활용하는 방법과 그 이점에 대해 설명합니다. 커밋 `767fc57007ae45f2f57e5ca3d1b4bc0e0d5d17e6`에서 추가된 코드를 기반으로 분석합니다.

## 2. 전략 패턴 복습

전략 패턴은 알고리즘군을 정의하고, 각 알고리즘을 캡슐화하여 상호 교환 가능하게 만드는 디자인 패턴입니다. 이 패턴은 알고리즘을 사용하는 클라이언트와 독립적으로 알고리즘을 변경할 수 있게 합니다.

### 전략 패턴의 구성 요소
- **Strategy(전략)**: 모든 전략에 공통적인 인터페이스
- **ConcreteStrategy(구체 전략)**: Strategy 인터페이스를 구현한 클래스
- **Context(컨텍스트)**: Strategy를 참조하여 사용하는 클래스

## 3. 익명 내부 클래스란?

익명 내부 클래스(Anonymous Inner Class)는 이름이 없는 클래스로, 선언과 동시에 객체를 생성합니다. 주로 인터페이스나 추상 클래스를 일회성으로 구현할 때 사용됩니다.

### 익명 내부 클래스의 특징
- 이름이 없는 클래스 (컴파일 시 자동으로 이름 생성)
- 한 번만 사용되는 경우에 적합
- 클래스 선언과 객체 생성을 동시에 수행
- 코드의 간결성 제공
- 외부 스코프의 final 또는 effectively final 변수에 접근 가능

## 4. 전략 패턴에 익명 내부 클래스 적용하기

### 4.1 일반 클래스로 구현한 전략 패턴 (기존 코드)

```java
// Strategy 인터페이스
public interface Strategy {
    void call();
}

// 구체적인 전략 클래스
public class StrategyLogic1 implements Strategy {
    @Override
    public void call() {
        log.info("비즈니스 로직1 실행");
    }
}

// 컨텍스트 클래스
public class ContextV1 {
    private final Strategy strategy;
    
    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }
    
    public void execute() {
        strategy.call();
    }
}

// 클라이언트 코드
@Test
void strategyV1() {
    Strategy strategyLogic1 = new StrategyLogic1();
    ContextV1 context1 = new ContextV1(strategyLogic1);
    context1.execute();
    
    Strategy strategyLogic2 = new StrategyLogic2();
    ContextV1 context2 = new ContextV1(strategyLogic2);
    context2.execute();
}
```

### 4.2 익명 내부 클래스로 구현한 전략 패턴 (신규 코드)

```java
/**
 * 전략 패턴을 사용하여 비즈니스 로직을 분리합니다.
 * 익명 내부 클래스를 사용하여 전략을 구현합니다.
 */
@Test
void strategyV2() {
    // Client Code
    Strategy strategyLogic1 = new Strategy() {
        @Override
        public void call() {
            log.info("비즈니스 로직1 실행");
        }
    };
    ContextV1 context1 = new ContextV1(strategyLogic1); // 전략을 주입합니다.
    log.info("strategyLogic1={}", strategyLogic1.getClass());
    context1.execute();

    Strategy strategyLogic2 = new Strategy() {
        @Override
        public void call() {
            log.info("비즈니스 로직2 실행");
        }
    };
    ContextV1 context2 = new ContextV1(strategyLogic2); // 전략을 주입합니다.
    log.info("strategyLogic2={}", strategyLogic2.getClass());
    context2.execute();
}
```

## 5. 두 방식의 비교

### 5.1 일반 클래스 방식

#### 장점
- 클래스 재사용 가능
- 코드 구조가 명확함
- 전략 클래스의 목적이 명시적으로 표현됨

#### 단점
- 일회성 사용에 비해 파일 수가 많아짐
- 간단한 로직에 비해 많은 코드 작성 필요

### 5.2 익명 내부 클래스 방식

#### 장점
- 클래스 정의와 객체 생성을 한 번에 처리
- 일회성 사용에 최적화됨
- 별도의 파일 생성 불필요
- 코드가 더 간결해짐
- 전략의 구현이 사용 위치에 가까워 이해하기 쉬움

#### 단점
- 재사용이 어려움
- 복잡한 로직에는 가독성이 떨어질 수 있음
- 디버깅이 상대적으로 어려울 수 있음

## 6. 익명 내부 클래스 사용 시 주의사항

### 6.1 클래스 이름과 구조
익명 내부 클래스는 이름이 없지만, 컴파일 시 자동으로 이름이 생성됩니다. 일반적으로 `외부클래스명$숫자.class` 형태의 파일이 생성됩니다.

```
// 예: 익명 내부 클래스 이름 예시
com.choongang.advanced.trace.strategy.StrategyTest$1
com.choongang.advanced.trace.strategy.StrategyTest$2
```

### 6.2 외부 변수 접근
익명 내부 클래스는 외부 스코프의 변수에 접근할 수 있지만, 해당 변수는 final 또는 effectively final이어야 합니다.

```java
String message = "Hello"; // effectively final
Strategy strategy = new Strategy() {
    @Override
    public void call() {
        log.info(message); // 외부 변수 접근 가능
    }
};
```

### 6.3 this 키워드
익명 내부 클래스 내에서 this는 익명 클래스 자신을 가리킵니다. 외부 클래스의 this에 접근하려면 `외부클래스명.this`를 사용해야 합니다.

```java
Strategy strategy = new Strategy() {
    @Override
    public void call() {
        log.info("익명 클래스의 this: {}", this);
        log.info("외부 클래스의 this: {}", StrategyTest.this);
    }
};
```

## 7. 익명 내부 클래스 활용의 발전 과정

### 7.1 일반 클래스 구현 (strategyV1)
```java
Strategy strategyLogic1 = new StrategyLogic1();
ContextV1 context1 = new ContextV1(strategyLogic1);
```

### 7.2 익명 내부 클래스 구현 (strategyV2)
```java
Strategy strategyLogic1 = new Strategy() {
    @Override
    public void call() {
        log.info("비즈니스 로직1 실행");
    }
};
ContextV1 context1 = new ContextV1(strategyLogic1);
```

### 7.3 익명 내부 클래스 인라인 구현
```java
ContextV1 context1 = new ContextV1(new Strategy() {
    @Override
    public void call() {
        log.info("비즈니스 로직1 실행");
    }
});
```

### 7.4 람다 표현식 (Java 8+)
```java
// Strategy 인터페이스가 함수형 인터페이스인 경우
ContextV1 context1 = new ContextV1(() -> log.info("비즈니스 로직1 실행"));
```

## 8. 실제 프레임워크에서의 익명 내부 클래스 활용 예시

### 8.1 Java Swing 이벤트 핸들러
```java
JButton button = new JButton("Click Me");
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked!");
    }
});
```

### 8.2 Java 스레드 생성
```java
Thread thread = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("Thread is running");
    }
});
thread.start();
```

### 8.3 Java Collections 정렬
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
Collections.sort(names, new Comparator<String>() {
    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length(); // 문자열 길이로 정렬
    }
});
```

## 9. 결론

익명 내부 클래스를 사용한 전략 패턴 구현은 코드의 간결성과 가독성을 향상시키는 좋은 방법입니다. 특히 일회성으로 사용되는 전략 객체를 정의할 때 유용합니다. 

일반 클래스와 익명 내부 클래스는 각각 상황에 맞게 선택하여 사용하는 것이 중요합니다:
- 여러 곳에서 재사용되는 전략은 일반 클래스로 구현
- 한 번만 사용되고 간단한 로직을 가진 전략은 익명 내부 클래스로 구현

또한 Java 8 이상에서는 전략 인터페이스가 함수형 인터페이스인 경우, 람다 표현식을 사용하여 더욱 간결하게 코드를 작성할 수 있습니다.

익명 내부 클래스는 전략 패턴뿐만 아니라 다양한 디자인 패턴과 프레임워크에서 널리 활용되고 있으며, 객체 지향 프로그래밍의 유연성을 높이는 중요한 기법 중 하나입니다.
