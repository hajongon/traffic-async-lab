# Version Naming

이 프로젝트는 구현 단계를 다음 이름으로 구분한다.

| 내부 식별자 | 표시 명칭 | 상태 | 의미 |
|---|---|---|---|
| `before` | 동기 통신 버전 | 구현됨 | HTTP 요청 안에서 주문, 결제, 알림을 모두 동기 처리하는 기준 버전 |
| `after` | 비동기 통신으로 개선한 버전 | 구현됨 | API p95 latency와 처리량은 개선됐지만 RabbitMQ/Worker backlog가 병목으로 남는 버전 |
| `async-improved` | 비동기 통신 개선 버전 | 추가 예정 | RabbitMQ/Worker 병목을 줄이는 개선 버전 |
| `async-improved-2` | 비동기 통신 개선 버전2 | 추가 예정 | 추가 병목 제거나 구조 개선을 이어갈 후속 버전 |

## 현재 식별자 유지 기준

디렉터리, Docker service, Prometheus job, Make target은 현재 `before`, `after` 식별자를 유지한다. 이 값들은 스크립트, 대시보드, 네트워크 이름, metric label에 연결되어 있어 한 번에 바꾸면 실행 환경이 크게 흔들릴 수 있다.

대신 사용자에게 노출되는 문서, 웹 화면, Grafana 제목, 비교 결과에서는 표시 명칭을 사용한다.
