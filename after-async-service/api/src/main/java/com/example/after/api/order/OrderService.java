package com.example.after.api.order;

import com.example.after.api.product.ProductRepository;
import com.example.after.api.queue.OrderEventPublisher;
import com.example.after.api.stock.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockService stockService;
    private final OrderEventPublisher orderEventPublisher;

    public OrderService(
            ProductRepository productRepository,
            OrderRepository orderRepository,
            StockService stockService,
            OrderEventPublisher orderEventPublisher
    ) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.stockService = stockService;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Transactional
    public Order create(CreateOrderRequest request) {
        productRepository.findById(request.productId()).orElseThrow();
        stockService.decrease(request.productId());
        try {
            Order order = orderRepository.save(new Order(request.productId(), request.userId()));
            orderEventPublisher.publish(new OrderEvent(order.getId(), order.getProductId(), order.getUserId()));
            return order;
        } catch (RuntimeException e) {
            stockService.compensate(request.productId());
            throw e;
        }
    }
}
