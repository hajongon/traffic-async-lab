#!/usr/bin/env bash
set -euo pipefail

docker compose -f after-async-service/docker-compose.yml down -v
docker compose -f after-async-service/docker-compose.yml up -d --build
