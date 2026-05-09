package com.example.after.worker.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
    private Instant updatedAt;

    @Version
    private long version;

    protected Order() {
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void processing() {
        this.status = OrderStatus.PROCESSING;
        this.updatedAt = Instant.now();
    }

    public void complete() {
        this.status = OrderStatus.COMPLETED;
        this.updatedAt = Instant.now();
    }

    public void fail(String reason) {
        this.status = OrderStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = Instant.now();
    }
}
