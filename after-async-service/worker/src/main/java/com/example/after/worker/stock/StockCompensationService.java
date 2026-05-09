package com.example.after.worker.stock;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockCompensationService {
    private static final String PREFIX = "stock:product:";

    private final StringRedisTemplate redisTemplate;

    public StockCompensationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void compensate(Long productId) {
        redisTemplate.opsForValue().increment(PREFIX + productId);
    }
}
