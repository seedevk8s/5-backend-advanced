# 템플릿 콜백 패턴(Template Callback Pattern)

## 개요
템플릿 콜백 패턴은 전략 패턴의 변형으로, 특정 작업을 수행하는 중에 변경되는 부분을 콜백 함수(인터페이스)로 분리하여 코드의 재사용성과 유연성을 높이는 디자인 패턴입니다. 이 패턴은 특히 자바 스프링 프레임워크에서 많이 사용되며, JdbcTemplate, RestTemplate 등이 대표적인 예입니다.

## 주요 구성 요소

1. **템플릿(Template)**: 변하지 않는 작업의 흐름(공통 로직)을 정의하는 클래스
   - 예: `TimeLogTemplate` - 실행 시간을 측정하는 템플릿

2. **콜백(Callback)**: 변하는 부분(비즈니스 로직)을 담당하는 인터페이스
   - 예: `Callback` 인터페이스와 그 구현체

## 코드 예제 분석

제공된 커밋에서는 `TemplateCallbackTest` 클래스에서 템플릿 콜백 패턴을 두 가지 방식으로 구현하고 있습니다:

### 1. 익명 내부 클래스를 사용한 방식 (V1)

```java
@Test
void templateCallbackV1() {
    TimeLogTemplate template = new TimeLogTemplate();
    template.execute(new Callback() {
        @Override
        public void call() {
            log.info("비즈니스 로직1 실행");
        }
    });
    template.execute(new Callback() {
        @Override
        public void call() {
            log.info("비즈니스 로직2 실행");
        }
    });
}
```

이 방식에서는 `Callback` 인터페이스의 익명 구현체를 생성하여 템플릿에 전달합니다.

### 2. 람다식을 사용한 방식 (V2)

```java
@Test
void templateCallbackV2() {
    TimeLogTemplate template = new TimeLogTemplate();
    template.execute(() -> log.info("비즈니스 로직1 실행"));
    template.execute(() -> log.info("비즈니스 로직2 실행"));
}
```

이 방식에서는 Java 8의 람다식을 사용하여 더 간결하게 콜백을 구현합니다. 인터페이스가 단일 메서드만 가지고 있는 경우(함수형 인터페이스) 람다식으로 대체할 수 있습니다.

## 템플릿 콜백 패턴의 장점

1. **코드 중복 제거**: 공통 로직(시간 측정 등)을 템플릿으로 분리하여 중복을 제거
2. **유연성 증가**: 콜백을 통해 다양한 비즈니스 로직을 유연하게 적용 가능
3. **SRP(단일 책임 원칙) 준수**: 변하는 부분과 변하지 않는 부분을 명확히 분리
4. **OCP(개방-폐쇄 원칙) 준수**: 기존 코드를 변경하지 않고 새로운 전략을 추가 가능

## 템플릿 메서드 패턴과의 차이점

- **템플릿 메서드 패턴**: 상속을 통해 기능을 확장
- **템플릿 콜백 패턴**: 위임을 통해 기능을 확장 (객체 합성)

## 활용 사례

1. **스프링 프레임워크**: JdbcTemplate, RestTemplate, TransactionTemplate 등
2. **성능 측정**: 메소드 실행 시간 측정 (위 예제)
3. **트랜잭션 처리**: 트랜잭션 시작, 커밋, 롤백 등의 공통 로직
4. **리소스 관리**: try-with-resources와 같은 자원 해제 로직

## 구현 시 고려사항

1. 콜백 인터페이스는 단일 메서드를 가진 함수형 인터페이스로 설계하면 람다식 활용 가능
2. 템플릿의 책임과 콜백의 책임을 명확히 분리
3. 콜백에 필요한 정보는 파라미터로 전달하거나 실행 컨텍스트를 통해 공유