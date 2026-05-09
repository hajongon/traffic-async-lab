#!/usr/bin/env bash
set -euo pipefail

docker compose -f before-sync-service/docker-compose.yml down -v
docker compose -f before-sync-service/docker-compose.yml up -d --build
