# 헬로트레이스 V2 문제점 및 대안 비교

## 현재 구조의 문제점과 대안 비교

| 문제점 | 설명 | 대안 | 장점 |
|--------|------|------|------|
| **코드 침투성** | • 비즈니스 로직에 트레이스 코드 침투<br>• 모든 메서드에 try-catch 중복<br>• trace 관련 코드 반복 | **AOP 적용** | • 비즈니스 코드 순수하게 유지<br>• @Trace 등 애노테이션만 추가<br>• 로깅 로직 중앙화 |
| **유지보수성 저하** | • 트레이스 로직 변경 시 모든 메서드 수정<br>• TraceId를 매번 파라미터로 전달<br>• 기능 추가 시 모든 곳에 적용 필요 | **ThreadLocal 활용** | • TraceId 자동 전파<br>• 파라미터 전달 불필요<br>• 코드 변경 최소화 |
| **관심사 분리 위반** | • 핵심 로직과 부가 기능 혼재<br>• 개발자 집중도 저하<br>• 코드 가독성 감소 | **프록시/데코레이터 패턴** | • 핵심 로직과 부가 기능 분리<br>• 코드 가독성 향상<br>• SRP 원칙 준수 |
| **확장성 제한** | • 새 메서드마다 동일한 코드 작성<br>• 분산 환경 추적 어려움<br>• 다양한 출력 형식 지원 제한 | **OpenTelemetry, Zipkin** | • 산업 표준 API 활용<br>• 마이크로서비스 환경 지원<br>• 다양한 시각화 도구 통합 |

## 단계적 개선 방안

| 단계 | 개선 방법 | 기대 효과 | 복잡도 |
|------|-----------|-----------|--------|
| **1단계** | **템플릿 메서드 패턴** 적용 | • 중복 코드 감소<br>• 예외 처리 통일<br>• 트레이스 로직 일관성 확보 | 낮음 |
| **2단계** | **ThreadLocal** 활용한 TraceId 전파 | • 파라미터 전달 제거<br>• 메서드 시그니처 단순화<br>• 코드 가독성 향상 | 중간 |
| **3단계** | **스프링 AOP** 도입 | • 완전한 관심사 분리<br>• 비즈니스 로직 순수성 확보<br>• 트레이스 적용 자동화 | 높음 |
| **4단계** | **분산 추적 시스템** 연동 | • 복잡한 시스템 전체 추적<br>• 다양한 시각화 도구 활용<br>• 성능 분석 고도화 | 매우 높음 |
