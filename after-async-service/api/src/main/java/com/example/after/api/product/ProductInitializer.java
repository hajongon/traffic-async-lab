package com.example.after.api.product;

import com.example.after.api.stock.StockService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductInitializer implements ApplicationRunner {
    private final ProductRepository productRepository;
    private final StockService stockService;
    private final int initialStock;

    public ProductInitializer(
            ProductRepository productRepository,
            StockService stockService,
            @Value("${app.initial-stock:1000000}") int initialStock
    ) {
        this.productRepository = productRepository;
        this.stockService = stockService;
        this.initialStock = initialStock;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Product product = productRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> productRepository.save(new Product("Async Lab Product", 10_000L, initialStock)));
        stockService.initialize(product.getId(), initialStock);
    }
}
