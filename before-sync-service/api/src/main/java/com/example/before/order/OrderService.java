package com.example.before.order;

import com.example.before.notification.NotificationService;
import com.example.before.payment.PaymentService;
import com.example.before.product.Product;
import com.example.before.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    public OrderService(
            ProductRepository productRepository,
            OrderRepository orderRepository,
            PaymentService paymentService,
            NotificationService notificationService
    ) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Order create(CreateOrderRequest request) {
        Product product = productRepository.findByIdForUpdate(request.productId()).orElseThrow();
        product.decreaseStock();

        Order order = orderRepository.save(new Order(product.getId(), request.userId(), OrderStatus.COMPLETED));
        paymentService.pay(order.getId());
        notificationService.send(order.getId());
        order.complete();
        return order;
    }
}
