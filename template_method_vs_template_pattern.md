# 템플릿 메서드 패턴 vs 템플릿 패턴: 용어 정리

디자인 패턴을 학습하다 보면 "템플릿 메서드 패턴(Template Method Pattern)"과 "템플릿 패턴(Template Pattern)"이라는 용어가 혼용되는 것을 볼 수 있습니다. 이 두 용어는 의미상 거의 같지만, **정확한 명칭과 사용에 있어 차이가 있습니다.**

---

## ✅ 공식 용어: 템플릿 메서드 패턴 (Template Method Pattern)

- GoF(Gang of Four) 디자인 패턴 중 하나로, **알고리즘의 골격(틀)은 상위 클래스에서 정의하고**, 일부 구체적인 단계는 **하위 클래스에서 오버라이드하여 정의**하는 구조입니다.
- 고전적인 OOP 설계에서 자주 사용됩니다.

---

## ✅ 비공식 용어: 템플릿 패턴 (Template Pattern)

- **"템플릿 메서드 패턴"의 줄임 표현**으로, 개발자들이 구어적으로 간단히 말할 때 사용하는 표현입니다.
- 공식 디자인 패턴 명칭은 아니며, 혼동을 줄이기 위해서는 "템플릿 메서드 패턴"이라는 전체 명칭을 사용하는 것이 좋습니다.

---

## 📌 주의: "템플릿"이라는 단어는 다양한 문맥에서 사용됨

| 문맥 | 의미 |
|------|------|
| 디자인 패턴 | Template Method Pattern |
| C++ | 제네릭을 위한 템플릿 기능 |
| HTML, React | UI 구조 정의용 템플릿 |
| Spring | `JdbcTemplate`, `RestTemplate` 등 템플릿 콜백 패턴 기반 클래스 |

---

## ✅ 결론 요약

| 질문 | 답변 |
|------|------|
| 템플릿 메서드 패턴과 템플릿 패턴은 같은가요? | ✔️ **의미는 같지만**, 정확한 용어는 **템플릿 메서드 패턴**입니다. |
