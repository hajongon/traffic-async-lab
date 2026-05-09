package com.example.before.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductInitializer implements ApplicationRunner {
    private final ProductRepository productRepository;
    private final int initialStock;

    public ProductInitializer(ProductRepository productRepository, @Value("${app.initial-stock:1000}") int initialStock) {
        this.productRepository = productRepository;
        this.initialStock = initialStock;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (productRepository.count() == 0) {
            productRepository.save(new Product("Async Lab Product", 10_000L, initialStock));
        }
    }
}
