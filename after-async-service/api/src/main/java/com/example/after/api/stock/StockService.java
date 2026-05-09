package com.example.after.api.stock;

import java.util.Collections;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private static final String PREFIX = "stock:product:";
    private static final DefaultRedisScript<Long> DECREASE_SCRIPT = new DefaultRedisScript<>("""
            local current = tonumber(redis.call('GET', KEYS[1]) or '-1')
            if current <= 0 then
              return -1
            end
            return redis.call('DECR', KEYS[1])
            """, Long.class);

    private final StringRedisTemplate redisTemplate;

    public StockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void initialize(Long productId, int stock) {
        redisTemplate.opsForValue().setIfAbsent(key(productId), String.valueOf(stock));
    }

    public int get(Long productId) {
        String value = redisTemplate.opsForValue().get(key(productId));
        return value == null ? 0 : Integer.parseInt(value);
    }

    public void decrease(Long productId) {
        Long result = redisTemplate.execute(DECREASE_SCRIPT, Collections.singletonList(key(productId)));
        if (result == null || result < 0) {
            throw new IllegalStateException("out of stock");
        }
    }

    public void compensate(Long productId) {
        redisTemplate.opsForValue().increment(key(productId));
    }

    private String key(Long productId) {
        return PREFIX + productId;
    }
}
