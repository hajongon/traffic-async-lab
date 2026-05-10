#!/usr/bin/env bash
set -euo pipefail

before_file="load-tests/results/before-stress.json"
after_file="load-tests/results/after-stress.json"

if [[ ! -f "$before_file" || ! -f "$after_file" ]]; then
  echo "Missing k6 result files. Run 'make load-before' and 'make load-after' first."
  exit 1
fi

python3 - "$before_file" "$after_file" <<'PY'
import json
import sys

def load(path):
    with open(path, encoding="utf-8") as f:
        data = json.load(f)
    metrics = data["metrics"]
    return {
        "requests": metrics["http_reqs"]["count"],
        "failed_rate": metrics["http_req_failed"]["value"],
        "avg_ms": metrics["http_req_duration"]["avg"],
        "p95_ms": metrics["http_req_duration"]["p(95)"],
    }

before = load(sys.argv[1])
after = load(sys.argv[2])

print("| target | requests | failed_rate | avg_ms | p95_ms |")
print("| --- | ---: | ---: | ---: | ---: |")
for name, item in (("동기 통신 버전", before), ("비동기 통신으로 개선한 버전", after)):
    print(f"| {name} | {item['requests']:.0f} | {item['failed_rate']:.4f} | {item['avg_ms']:.2f} | {item['p95_ms']:.2f} |")
PY
