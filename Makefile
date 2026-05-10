up-monitoring:
	docker compose -f monitoring/docker-compose.monitoring.yml up -d --build

up-before:
	docker compose -f before-sync-service/docker-compose.yml up -d --build

up-after:
	docker compose -f after-async-service/docker-compose.yml up -d --build

reset-before:
	bash scripts/reset-before.sh

reset-after:
	bash scripts/reset-after.sh

load-before:
	bash scripts/run-before-load.sh

load-after:
	bash scripts/run-after-load.sh

run-before-load: load-before

run-after-load: load-after

compare:
	bash scripts/compare-result.sh

down-before:
	docker compose -f before-sync-service/docker-compose.yml down -v

down-after:
	docker compose -f after-async-service/docker-compose.yml down -v

down-monitoring:
	docker compose -f monitoring/docker-compose.monitoring.yml down -v

down:
	docker compose -f before-sync-service/docker-compose.yml down -v
	docker compose -f after-async-service/docker-compose.yml down -v
	docker compose -f monitoring/docker-compose.monitoring.yml down -v
