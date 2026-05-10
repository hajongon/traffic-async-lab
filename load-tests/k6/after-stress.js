import http from 'k6/http';
import { check } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 20 },
    { duration: '1m', target: 50 },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    http_req_failed: ['rate<0.20'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8082';

http.setResponseCallback(http.expectedStatuses({ min: 200, max: 399 }, 400));

export default function () {
  const res = http.post(`${BASE_URL}/orders`, JSON.stringify({ productId: 1, userId: `stress-${__VU}-${__ITER}` }), {
    headers: { 'Content-Type': 'application/json' },
  });
  check(res, { 'accepted or stock exhausted': (r) => r.status === 202 || r.status === 400 });
}
