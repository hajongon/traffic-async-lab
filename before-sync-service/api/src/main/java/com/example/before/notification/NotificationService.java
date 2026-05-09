package com.example.before.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void send(Long orderId) {
        sleep(200);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("notification interrupted", e);
        }
    }
}
