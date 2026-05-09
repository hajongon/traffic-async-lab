package com.example.after.api.metrics;

import com.example.after.api.order.OrderRepository;
import com.example.after.api.order.OrderStatus;
import com.example.after.api.product.ProductRepository;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public StatsController(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return Map.of(
                "products", productRepository.count(),
                "orders", orderRepository.count(),
                "pending", orderRepository.countByStatus(OrderStatus.PENDING),
                "processing", orderRepository.countByStatus(OrderStatus.PROCESSING),
                "completed", orderRepository.countByStatus(OrderStatus.COMPLETED),
                "failed", orderRepository.countByStatus(OrderStatus.FAILED)
        );
    }
}
