package com.example.after.api.order;

public record OrderEvent(Long orderId, Long productId, String userId) {
}
