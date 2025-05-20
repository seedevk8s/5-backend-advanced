# GOF 전략 패턴(Strategy Pattern) 상세 분석

## 1. 전략 패턴 개요

### 1.1 정의
전략 패턴(Strategy Pattern)은 GOF의 디자인 패턴 중 하나로, 알고리즘군을 정의하고 각각을 캡슐화하여 교환 가능하게 만드는 패턴입니다. 이 패턴을 사용하면 알고리즘을 사용하는 클라이언트와 독립적으로 알고리즘을 변경할 수 있습니다.

### 1.2 의도
- 동일 계열의 알고리즘군을 정의
- 각 알고리즘을 캡슐화하여 상호 교환 가능하게 함
- 알고리즘을 사용하는 클라이언트와 알고리즘을 독립적으로 변경 가능

### 1.3 별칭
- Policy Pattern (정책 패턴)

## 2. 전략 패턴의 구조

### 2.1 핵심 구성 요소
1. **Strategy (전략)**: 모든 지원되는 알고리즘에 대한 공통 인터페이스
2. **ConcreteStrategy (구체적인 전략)**: Strategy 인터페이스를 구현한 클래스
3. **Context (컨텍스트)**: Strategy 객체를 참조하고 이를 사용하는 클래스

### 2.2 패턴 구현 방식
전략 패턴은 두 가지 주요 방식으로 구현할 수 있습니다:

1. **필드로 전략을 보관하는 방식 (ContextV1)**
   ```java
   public class ContextV1 {
       private final Strategy strategy;

       public ContextV1(Strategy strategy) {
           this.strategy = strategy;
       }

       public void execute() {
           // 전략 실행 전 공통 로직
           strategy.call();
           // 전략 실행 후 공통 로직
       }
   }
   ```

2. **파라미터로 전략을 전달받는 방식 (ContextV2)**
   ```java
   public class ContextV2 {
       public void execute(Strategy strategy) {
           // 전략 실행 전 공통 로직
           strategy.call();
           // 전략 실행 후 공통 로직
       }
   }
   ```

## 3. 전략 구현 방법

### 3.1 일반 클래스를 사용한 구현
```java
public interface Strategy {
    void call();
}

public class StrategyLogic1 implements Strategy {
    @Override
    public void call() {
        System.out.println("비즈니스 로직 1 실행");
    }
}

public class StrategyLogic2 implements Strategy {
    @Override
    public void call() {
        System.out.println("비즈니스 로직 2 실행");
    }
}
```

### 3.2 익명 내부 클래스를 사용한 구현
```java
Strategy strategy = new Strategy() {
    @Override
    public void call() {
        System.out.println("익명 내부 클래스로 구현한 비즈니스 로직 실행");
    }
};
```

### 3.3 람다를 사용한 구현 (Java 8+)
```java
// Strategy 인터페이스가 함수형 인터페이스인 경우
Strategy strategy = () -> System.out.println("람다로 구현한 비즈니스 로직 실행");
```

## 4. 두 가지 컨텍스트 구현 방식 비교

### 4.1 ContextV1 (필드에 전략을 보관)

#### 장점
- 컨텍스트 생성 시점에 전략이 주입되므로, 컨텍스트를 생성한 이후에는 전략을 신경 쓰지 않고 사용 가능
- 컨텍스트와 전략의 의존관계가 명확하게 드러남

#### 단점
- 전략을 변경하려면 새로운 컨텍스트 객체를 생성하거나 setter를 통해 전략을 변경해야 함
- 컨텍스트 객체 하나당 하나의 전략만 사용 가능

#### 사용 예제
```java
// 전략 객체 생성
Strategy strategyLogic1 = new StrategyLogic1();

// 컨텍스트에 전략 주입
ContextV1 context1 = new ContextV1(strategyLogic1);

// 컨텍스트 실행
context1.execute();

// 다른 전략으로 변경하려면 새로운 컨텍스트 생성
Strategy strategyLogic2 = new StrategyLogic2();
ContextV1 context2 = new ContextV1(strategyLogic2);
context2.execute();
```

### 4.2 ContextV2 (파라미터로 전략을 전달)

#### 장점
- 컨텍스트 객체를 한 번만 생성하고 여러 전략을 유연하게 전달 가능
- 실행할 때마다 다른 전략을 선택 가능

#### 단점
- 실행할 때마다 전략을 지정해야 함
- 전략 인스턴스를 계속 생성해야 한다면 자원 낭비 가능성

#### 사용 예제
```java
// 컨텍스트 생성 (전략 없이)
ContextV2 context = new ContextV2();

// 전략 객체를 생성하고 컨텍스트 실행
context.execute(new StrategyLogic1());
context.execute(new StrategyLogic2());

// 익명 내부 클래스 사용
context.execute(new Strategy() {
    @Override
    public void call() {
        System.out.println("익명 내부 클래스로 구현한 전략");
    }
});

// 람다 사용 (Java 8+)
context.execute(() -> System.out.println("람다로 구현한 전략"));
```

## 5. 전략 패턴의 장단점

### 5.1 장점
1. **알고리즘 교체 용이성**: 런타임에 알고리즘 교체 가능
2. **알고리즘 캡슐화**: 알고리즘의 세부 구현을 클라이언트로부터 숨김
3. **조건문 감소**: 복잡한 조건문을 전략 객체로 대체
4. **확장성**: 새로운 전략을 쉽게 추가 가능 (OCP 원칙)
5. **테스트 용이성**: 각 전략을 독립적으로 테스트 가능

### 5.2 단점
1. **클래스 증가**: 전략마다 클래스가 필요하므로 클래스 수가 증가
2. **전략 선택 책임**: 클라이언트가 적절한 전략을 선택해야 함
3. **통신 오버헤드**: 전략 간에 데이터 공유가 필요한 경우 오버헤드 발생
4. **메모리 사용량**: 많은 수의 전략 객체가 생성되면 메모리 사용량 증가

## 6. 전략 패턴 활용 예시

### 6.1 정렬 알고리즘
- 버블 정렬, 퀵 정렬, 병합 정렬 등 다양한 정렬 알고리즘을 전략으로 구현
- 데이터 특성에 따라 적절한 정렬 알고리즘 선택

### 6.2 결제 시스템
- 신용카드, 페이팔, 계좌이체 등 다양한 결제 방식을 전략으로 구현
- 사용자 선택에 따라 적절한 결제 방식 적용

### 6.3 로그인 인증
- 일반 로그인, 소셜 로그인, 생체 인증 등 다양한 인증 방식을 전략으로 구현
- 상황에 따라 적절한 인증 방식 선택

### 6.4 로깅 및 추적 시스템
- 콘솔 로깅, 파일 로깅, 데이터베이스 로깅 등 다양한 로깅 방식을 전략으로 구현
- 환경에 따라 적절한 로깅 방식 선택

## 7. 관련 패턴

### 7.1 템플릿 메서드 패턴과의 관계
- **유사점**: 둘 다 알고리즘의 일부를 변경 가능하게 함
- **차이점**: 
  - 템플릿 메서드 패턴은 상속을 통해 알고리즘 구조 재사용
  - 전략 패턴은 합성(composition)을 통해 알고리즘 교체 가능

### 7.2 상태 패턴과의 관계
- **유사점**: 둘 다 객체의 행동을 캡슐화하고 위임을 사용
- **차이점**:
  - 상태 패턴은 객체의 상태에 따라 자동으로 행동 변경
  - 전략 패턴은 클라이언트가 명시적으로 알고리즘 선택

### 7.3 명령 패턴과의 관계
- **유사점**: 둘 다 행동을 캡슐화
- **차이점**:
  - 명령 패턴은 요청 자체를 객체로 캡슐화
  - 전략 패턴은 알고리즘을 캡슐화

## 8. 실제 프레임워크에서의 전략 패턴 사용 예

### 8.1 Java의 Collections.sort()
```java
List<String> list = new ArrayList<>();
// Comparator는 정렬 전략을 나타내는 인터페이스
Collections.sort(list, new Comparator<String>() {
    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2);
    }
});

// 람다 사용 (Java 8+)
Collections.sort(list, (o1, o2) -> o1.compareTo(o2));
```

### 8.2 Spring의 Resource 로딩 전략
```java
// ResourceLoader 인터페이스는 리소스 로딩 전략을 정의
ResourceLoader resourceLoader = new ClassPathResourceLoader();
Resource resource = resourceLoader.getResource("classpath:config.xml");

// 다른 전략 사용
resourceLoader = new FileSystemResourceLoader();
resource = resourceLoader.getResource("file:config.xml");
```

## 9. 결론

전략 패턴은 알고리즘을 캡슐화하고 교체 가능하게 만들어 시스템의 유연성과 확장성을 높이는 강력한 디자인 패턴입니다. 특히 행동이 런타임에 변경되어야 하거나, 조건에 따라 다른 알고리즘을 선택해야 하는 경우에 유용합니다.

필드로 전략을 보관하는 방식(ContextV1)과 파라미터로 전략을 전달받는 방식(ContextV2)은 각각 장단점이 있으며, 상황에 따라 적절한 방식을 선택해야 합니다. 또한 전략 구현 방식도 일반 클래스, 익명 내부 클래스, 람다 등 다양한 방법을 활용할 수 있습니다.

전략 패턴은 객체 지향 설계의 핵심 원칙인 "변하는 것과 변하지 않는 것을 분리"하고 "상속보다는 합성을 사용"하는 좋은 예시이며, 현대적인 소프트웨어 개발에서 널리 활용되고 있습니다.
