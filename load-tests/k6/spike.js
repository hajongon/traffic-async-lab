import http from 'k6/http';
import { check } from 'k6';

export const options = {
  stages: [
    { duration: '10s', target: 10 },
    { duration: '20s', target: 100 },
    { duration: '20s', target: 100 },
    { duration: '10s', target: 0 },
  ],
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8082';

export default function () {
  const res = http.post(`${BASE_URL}/orders`, JSON.stringify({ productId: 1, userId: `spike-${__VU}-${__ITER}` }), {
    headers: { 'Content-Type': 'application/json' },
  });
  check(res, { 'handled': (r) => r.status === 201 || r.status === 202 || r.status === 400 });
}
