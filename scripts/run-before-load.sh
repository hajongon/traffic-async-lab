#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8081}"
K6_BIN="${K6_BIN:-k6}"
if ! command -v "$K6_BIN" >/dev/null 2>&1 && [[ -x "tools/k6" ]]; then
  K6_BIN="tools/k6"
fi

mkdir -p load-tests/results
"$K6_BIN" run --summary-export load-tests/results/before-stress.json -e BASE_URL="$BASE_URL" load-tests/k6/before-stress.js
