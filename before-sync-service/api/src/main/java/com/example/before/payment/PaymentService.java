package com.example.before.payment;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public void pay(Long orderId) {
        sleep(300);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("payment interrupted", e);
        }
    }
}
