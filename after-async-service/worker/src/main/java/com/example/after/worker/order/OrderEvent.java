package com.example.after.worker.order;

public record OrderEvent(Long orderId, Long productId, String userId) {
}
