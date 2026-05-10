# traffic-async-lab

Spring Boot 기반 동기 주문 처리와 비동기 큐 기반 주문 처리를 같은 조건에서 비교하는 실습 프로젝트입니다.

## 버전 명칭

| 내부 식별자 | 표시 명칭 | 상태 | 설명 |
|---|---|---|---|
| `before` | 동기 통신 버전 | 구현됨 | HTTP 요청 안에서 주문, 결제, 알림을 모두 동기 처리하는 기준 버전 |
| `after` | 비동기 통신으로 개선한 버전 | 구현됨 | API p95 latency와 처리량은 개선됐지만 RabbitMQ/Worker backlog가 병목으로 남는 버전 |
| `async-improved` | 비동기 통신 개선 버전 | 추가 예정 | RabbitMQ/Worker 병목을 줄이는 개선 버전 |
| `async-improved-2` | 비동기 통신 개선 버전2 | 추가 예정 | 향후 추가될 후속 개선 버전 |

자세한 명칭 기준은 [VERSIONING.md](VERSIONING.md)를 참고합니다.

## 구조

- `before-sync-service/api`: 동기 통신 버전 API
- `after-async-service/api`: 비동기 통신으로 개선한 버전 API
- `after-async-service/worker`: 비동기 통신으로 개선한 버전 Worker
- `monitoring`: Prometheus, Grafana 설정
- `load-tests/k6`: k6 smoke, stress, spike, soak 테스트
- `before-sync-service/web`, `after-async-service/web`: 수동 확인용 HTML 화면

## 실행

```bash
make up-before
curl http://localhost:8081/health
curl http://localhost:8081/products
curl -X POST http://localhost:8081/orders \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"userId":"user-123"}'
```

```bash
make up-after
curl http://localhost:8082/health
curl http://localhost:8082/products
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"userId":"user-123"}'
```

초기 재고를 늘려 실행하려면 `APP_INITIAL_STOCK`을 지정합니다.

```bash
APP_INITIAL_STOCK=1000000 bash scripts/reset-before.sh
APP_INITIAL_STOCK=1000000 bash scripts/reset-after.sh
```

## 포트

- 동기 통신 버전 API: `8081`
- 비동기 통신으로 개선한 버전 API: `8082`
- 비동기 통신으로 개선한 버전 Worker actuator: `8083`
- PostgreSQL before: `5432`
- PostgreSQL after: `5433`
- Redis after: `6379`
- RabbitMQ AMQP: `5672`
- RabbitMQ Management: `15672` (`after` / `after`)
- RabbitMQ Prometheus metrics: `15692`
- Prometheus: `9090`
- Grafana: `3000` (`admin` / `admin`)

## 모니터링

동기 통신 버전과 비동기 통신으로 개선한 버전을 먼저 실행한 뒤 모니터링을 올립니다.

```bash
make up-before
make up-after
make up-monitoring
```

Prometheus는 다음 대상을 scrape합니다.

- `before-api:8081/actuator/prometheus`
- `after-api:8082/actuator/prometheus`
- `after-worker:8083/actuator/prometheus`
- `rabbitmq-after:15692/metrics`

## 부하 테스트

로컬에 k6가 설치되어 있어야 합니다.

```bash
make load-before
make load-after
make compare
```

결과 파일은 `load-tests/results` 아래에 저장됩니다.

## 수동 웹 확인

HTML 파일을 브라우저에서 열어 주문과 통계를 확인할 수 있습니다.

- `before-sync-service/web/index.html`
- `after-async-service/web/index.html`

## 비교 관점

- 동기 통신 버전: 트랜잭션 안에서 결제 300ms, 알림 200ms 지연을 수행하므로 API latency와 DB connection 점유가 증가합니다.
- 비동기 통신으로 개선한 버전: API는 Redis 재고 차감과 메시지 발행 후 `202 Accepted`를 반환하고, Worker가 느린 후속 처리를 담당합니다.
- 비동기 통신 개선 버전: RabbitMQ/Worker 병목을 줄이는 후속 버전으로 추가 예정입니다.
- 핵심 관측값은 API p95 latency, error rate, DB connection, Tomcat thread, queue depth, worker throughput입니다.
