import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 1,
  iterations: 5,
  thresholds: {
    http_req_failed: ['rate<0.05'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8081';

export default function () {
  const product = http.get(`${BASE_URL}/products/1`);
  check(product, { 'product ok': (r) => r.status === 200 });

  const order = http.post(`${BASE_URL}/orders`, JSON.stringify({ productId: 1, userId: `smoke-${__VU}-${__ITER}` }), {
    headers: { 'Content-Type': 'application/json' },
  });
  check(order, { 'order created': (r) => r.status === 201 });
  sleep(1);
}
