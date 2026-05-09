import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 20,
  duration: '10m',
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8082';

export default function () {
  const res = http.post(`${BASE_URL}/orders`, JSON.stringify({ productId: 1, userId: `soak-${__VU}-${__ITER}` }), {
    headers: { 'Content-Type': 'application/json' },
  });
  check(res, { 'handled': (r) => r.status === 201 || r.status === 202 || r.status === 400 });
  sleep(1);
}
