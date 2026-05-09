package com.example.after.api.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private long price;
    private int initialStock;

    protected Product() {
    }

    public Product(String name, long price, int initialStock) {
        this.name = name;
        this.price = price;
        this.initialStock = initialStock;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public int getInitialStock() {
        return initialStock;
    }
}
