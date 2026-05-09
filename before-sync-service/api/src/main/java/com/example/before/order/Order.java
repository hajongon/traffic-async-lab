package com.example.before.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity(name = "orders")
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(length = 512)
    private String failureReason;

    private Instant createdAt;
    private Instant completedAt;

    protected Order() {
    }

    public Order(Long productId, String userId, OrderStatus status) {
        this.productId = productId;
        this.userId = userId;
        this.status = status;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getUserId() {
        return userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void complete() {
        this.status = OrderStatus.COMPLETED;
        this.completedAt = Instant.now();
    }
}
