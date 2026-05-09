package com.example.before.metrics;

import com.example.before.order.OrderRepository;
import com.example.before.order.OrderStatus;
import com.example.before.product.ProductRepository;
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
                "completed", orderRepository.countByStatus(OrderStatus.COMPLETED),
                "failed", orderRepository.countByStatus(OrderStatus.FAILED)
        );
    }
}
